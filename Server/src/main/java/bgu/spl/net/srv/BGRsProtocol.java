package bgu.spl.net.srv;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.srv.Messages.Message;

public class BGRsProtocol implements MessagingProtocol<Message> {
        boolean shouldTerminate = false;
        boolean logIn = false;
        String userName;
        private Database  dataBase= Database.getInstance();
        @Override
        public Message process(Message msg) {
            msg.executeCommand(this);

            return null;
        }

    public boolean isLogIn() {
        return logIn;
    }

    public String getUserName() {
        return userName;
    }

    public Database getDataBase() {
        return dataBase;
    }

    public void setLogIn(boolean logIn) {
        this.logIn = logIn;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
        public boolean shouldTerminate() {
            return shouldTerminate;
        }
    }
