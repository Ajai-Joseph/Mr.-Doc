package com.example.mrdoclogin;

    public class Messages {
        String message,senderId;

        public Messages()
        {

        }

        public Messages(String message, String senderId) {
            this.message = message;
            this.senderId = senderId;

        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }


    }


