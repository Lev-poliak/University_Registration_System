package bgu.spl.net.srv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private static final Database singleton = new Database();
	private final ConcurrentHashMap<String,student> studentsByUsername;
	private final ConcurrentHashMap<Integer, course> courses;
	private final Vector<Integer> coursesVector;
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	//to prevent user from creating new Database
	private Database() {
		// TODO: implement
		studentsByUsername = new ConcurrentHashMap<String, student>();
		courses = new ConcurrentHashMap<>();
		coursesVector = new Vector<>();

		String path = System.getProperty("user.dir");
		initialize(path + "/Courses.txt");
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		return singleton;
	}

	/**
	 * loades the courses from the file path specified
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) {
		File coursesFile = new File(coursesFilePath);
		Scanner coursesFileScanner = null;
		try {
			coursesFileScanner = new Scanner(coursesFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		while(coursesFileScanner.hasNextLine()){
			// get the data as string array
			String courseFileLine = coursesFileScanner.nextLine();
			String[] courseData = courseFileLine.split("\\|");

			// get the data
			int courseNum = Integer.parseInt(courseData[0]);
			String courseName = courseData[1];
			String kdamCoursesAs1String = courseData[2].substring(1, courseData[2].length() - 2);
			String[] kdamCourses = kdamCoursesAs1String.split(",");
			LinkedList<Integer> kdamCoursesList = new LinkedList<>();
			if (kdamCourses[0].compareTo("") != 0){
				for(String kdamCourse: kdamCourses){
					kdamCoursesList.push(Integer.parseInt(kdamCourse));
				}
			}
			int numOfMaxStudents = Integer.parseInt(courseData[3]);

			// create a new course
			course course = new course(courseNum, courseName, kdamCoursesList, numOfMaxStudents);

			// add the course to out lists
			courses.put(courseNum, course);
			coursesVector.add(courseNum);
		}

		return false;
	}

	public boolean registerUser(String name,String password ,boolean admin){
		rwl.writeLock().lock();
		if (!studentsByUsername.contains(name)){
			studentsByUsername.put(name, new student(name, password, admin));
			rwl.writeLock().unlock();
			return true;
		}
			rwl.writeLock().unlock();
		return false;

	}

	public boolean loginAccount(String name ,String password){
		if (!studentsByUsername.contains(name)){
			return false;
		}
		return studentsByUsername.get(name).login(password);
	}

	public boolean logOut(String name){
		studentsByUsername.get(name).logOut();
		return true;
	}
	public String kdamCoursesString(Integer courseNum){
		return courses.get(courseNum).kdamCoursesString();

	}
	public boolean isCourseExist(Integer num){
		return courses.contains(num);
	}

	public boolean registerCourse(String name,Integer courseToRegister){

		student tempStudent = studentsByUsername.get(name);

		if (tempStudent.containAllKdamCourses(courses.get(courseToRegister).KdamCoursesList) & !tempStudent.isAdmin()){
			rwl.writeLock().lock();
			course CourseNeedToRegister = courses.get(courseToRegister);
			if (CourseNeedToRegister.numOfMaxStudents > CourseNeedToRegister.numOfRegisterdStudents.get()){
				CourseNeedToRegister.addListOfRegisteredStudents(name);
				tempStudent.addCourse(courseToRegister);
				return true;
			}
			rwl.writeLock().unlock();
		}
		return false;
	}

	public boolean unRegister(String name,Integer courseToUnregister){
		if (isRegisterd(name,courseToUnregister)){
			rwl.writeLock().lock();
			courses.get(courseToUnregister).uregisterStudent(name);
			studentsByUsername.get(name).removeCourse(courseToUnregister);
			rwl.writeLock().unlock();
			return true;
		}
		return false;
	}

	public boolean isRegisterd(String name,Integer courseToCheck){
		student studentToCheck = studentsByUsername.get(name);
		return studentToCheck.isRegistered(courseToCheck);
	}

	public boolean isAdmin(String name){
		return studentsByUsername.get(name).isAdmin();
	}

	public String courseToString(Integer courseNum){
		return courses.get(courseNum).courseToString();
	}

	public String StudentToString(String name){
		StringBuilder build = new StringBuilder();
		build.append(name);
		rwl.readLock().lock();
		student studentData = studentsByUsername.get(name);
		rwl.readLock().unlock();
		build.append(studentData.studentToString());
		build.append('\0');
		return build.toString();
	}

	public String studentCourses(String name){
		StringBuilder build = new StringBuilder();
		build.append("[");
		rwl.readLock().lock();
		student studentData = studentsByUsername.get(name);
		rwl.readLock().unlock();
		build.append(studentData.studentToString());
		build.append("]");
		build.append('\0');
		return build.toString();
	}



	Vector<Integer> getCoursesVector(){
		return coursesVector;
	}


	// TODO: move to another file
	public class course{
		private final int courseNum;
		private final String courseName;
		private final LinkedList<Integer> KdamCoursesList;
		private final int numOfMaxStudents;
		private final AtomicInteger numOfRegisterdStudents;
		private final LinkedList<String> ListOfRegisteredStudents;
		private final ReentrantReadWriteLock rwl1 = new ReentrantReadWriteLock();

		public course(int courseNum, String courseName,
					  LinkedList<Integer> KdamCoursesList, int numOfMaxStudents){
			this.courseNum = courseNum;
			this.courseName = courseName;
			this.KdamCoursesList = KdamCoursesList;
			this.numOfMaxStudents = numOfMaxStudents;
			numOfRegisterdStudents = new AtomicInteger(0);
			ListOfRegisteredStudents = new LinkedList<>();
		}

		public void addListOfRegisteredStudents(String name){
			rwl.writeLock().lock();
			ListOfRegisteredStudents.add(name);
			numOfRegisterdStudents.compareAndSet(numOfRegisterdStudents.get(),numOfRegisterdStudents.get()+1);
			rwl.writeLock().unlock();
		}


		public String courseToString(){
			rwl1.readLock().lock();
			StringBuilder stats  = new StringBuilder();
			addString( stats,courseNum);
			addString(stats,courseName);
			Integer available = numOfMaxStudents-numOfRegisterdStudents.get();
			addString( stats,available);
			addString( stats,numOfMaxStudents);
			stats.append("|");
			rwl1.readLock().unlock();
			if (!ListOfRegisteredStudents.isEmpty()) {
				Iterator<String> iter = ListOfRegisteredStudents.iterator();
				stats.append(iter);
					while (iter.hasNext()) {
						iter.next();
						stats.append("|");
						stats.append(iter);
					}
				stats.append('\0');
			}
			return stats.toString();
		}

		private StringBuilder addString(StringBuilder out, Integer num){
			out.append(num);
			out.append("|");
			return out;
		}

		private StringBuilder addString(StringBuilder out, String name){
			out.append(name);
			out.append("|");
			return out;
		}

		public String kdamCoursesString(){
			StringBuilder strbul  = new StringBuilder();
			Iterator<Integer> iter = KdamCoursesList.iterator();
			while(iter.hasNext()) {
				strbul.append(iter.next());
				if(iter.hasNext()){
					strbul.append(",");
				}
			}
			if (!(strbul.length()==0)){
				strbul.append('\0');
			}
			return strbul.toString();
		}

		public void uregisterStudent(String name){
			rwl.writeLock().lock();
			ListOfRegisteredStudents.remove(name);
			numOfRegisterdStudents.compareAndSet(numOfRegisterdStudents.get(),numOfRegisterdStudents.get()-1);
			rwl.writeLock().unlock();
		}
	}

// TODO: move to another file
	public static class student {
		private boolean logged = false;
		private final String passWord;
		private final LinkedList<Integer> currentCourses;
		private final boolean admin;
		private final Vector<Integer> coursesVector;

		public student(String _userName,String _passWord,boolean _admin){
			passWord = _passWord;
			currentCourses = new LinkedList<>();
			admin = _admin;
			coursesVector = Database.getInstance().getCoursesVector();
		}

		public synchronized void addCourse(Integer courseNum){
			currentCourses.add(courseNum);
			Iterator<Integer> studentCoursesIter = currentCourses.iterator();
			Iterator<Integer> databaseCoursesIter = coursesVector.iterator();

			int indexToAddCourse = 0;
			int databaseCoursesIterData = databaseCoursesIter.next();
			if (!studentCoursesIter.hasNext()){
				// the first course of the student
				currentCourses.add(courseNum);
			}
			int studentCoursesIterData = studentCoursesIter.next();
			while(courseNum != databaseCoursesIterData && studentCoursesIter.hasNext()){
				if (studentCoursesIterData == databaseCoursesIterData){
					studentCoursesIterData = studentCoursesIter.next();
					indexToAddCourse++;
				}
				databaseCoursesIterData = databaseCoursesIter.next();
			}

			if (!studentCoursesIter.hasNext()){
				while(courseNum != databaseCoursesIterData && studentCoursesIterData != databaseCoursesIterData){
					databaseCoursesIterData = databaseCoursesIter.next();
				}
				if (studentCoursesIterData == databaseCoursesIterData) {
					// add the course at the end of the list
					currentCourses.add(courseNum);
					return;
				}
			}

			// add the course at intended index
			currentCourses.add(indexToAddCourse, courseNum);
		}

		public synchronized void removeCourse(Integer course){
			currentCourses.remove(course);
		}

		public LinkedList<Integer> getCurrentCourses() {
			return currentCourses;
		}

		public boolean isAdmin() {
			return admin;
		}


		public synchronized boolean login(String _passWord) {
			if (logged ||!passWord.equals(_passWord)) {
				return false;
			} else {
				logged=true;
				return true;
			}
		}

		public void logOut(){
			logged =false;
		}

		public boolean containAllKdamCourses(LinkedList<Integer> kdam){
			Iterator<Integer> iter = kdam.iterator();
			while (iter.hasNext()){
				if(!currentCourses.contains(iter)){
					return false;
				}
				iter.next();
			}
			return true;
		}

		public synchronized String studentToString(){
			StringBuilder courseString = new StringBuilder();
			if (!currentCourses.isEmpty()) {
				Iterator<Integer> iter = currentCourses.iterator();
				courseString.append(iter);
				while (iter.hasNext()){
					iter.next();
					courseString.append(",");
					courseString.append(iter);
				}
				courseString.append('\0');
			}
			return courseString.toString();
		}

		public boolean isRegistered(Integer course){
			return currentCourses.contains(course);
		}
	}


}
