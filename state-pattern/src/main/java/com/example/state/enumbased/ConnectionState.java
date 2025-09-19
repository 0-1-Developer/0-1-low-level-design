package com.example.state.enumbased;

public enum ConnectionState {
    DISCONNECTED {
        @Override
        public ConnectionState connect(Connection context) {
            System.out.println("Initiating connection...");
            context.setMessage("Connecting to server...");
            return CONNECTING;
        }
        
        @Override
        public ConnectionState disconnect(Connection context) {
            System.out.println("Already disconnected");
            return this;
        }
        
        @Override
        public ConnectionState sendData(Connection context, String data) {
            System.out.println("Cannot send data - not connected");
            return this;
        }
        
        @Override
        public ConnectionState receiveData(Connection context) {
            System.out.println("Cannot receive data - not connected");
            return this;
        }
        
        @Override
        public ConnectionState timeout(Connection context) {
            System.out.println("No connection to timeout");
            return this;
        }
    },
    
    CONNECTING {
        @Override
        public ConnectionState connect(Connection context) {
            System.out.println("Connection already in progress");
            return this;
        }
        
        @Override
        public ConnectionState disconnect(Connection context) {
            System.out.println("Canceling connection attempt");
            context.setMessage("Connection canceled");
            return DISCONNECTED;
        }
        
        @Override
        public ConnectionState sendData(Connection context, String data) {
            System.out.println("Cannot send data - connection in progress");
            return this;
        }
        
        @Override
        public ConnectionState receiveData(Connection context) {
            if (context.getMessage() != null && context.getMessage().contains("ACK")) {
                System.out.println("Connection established!");
                context.setMessage("Connected");
                return CONNECTED;
            }
            System.out.println("Waiting for connection acknowledgment");
            return this;
        }
        
        @Override
        public ConnectionState timeout(Connection context) {
            System.out.println("Connection timeout - returning to disconnected");
            context.setMessage("Connection timeout");
            return DISCONNECTED;
        }
    },
    
    CONNECTED {
        @Override
        public ConnectionState connect(Connection context) {
            System.out.println("Already connected");
            return this;
        }
        
        @Override
        public ConnectionState disconnect(Connection context) {
            System.out.println("Disconnecting from server...");
            context.setMessage("Disconnected");
            return DISCONNECTED;
        }
        
        @Override
        public ConnectionState sendData(Connection context, String data) {
            System.out.println("Sending data: " + data);
            context.incrementPacketsSent();
            context.setLastData(data);
            return this;
        }
        
        @Override
        public ConnectionState receiveData(Connection context) {
            System.out.println("Receiving data...");
            context.incrementPacketsReceived();
            return this;
        }
        
        @Override
        public ConnectionState timeout(Connection context) {
            System.out.println("Connection lost due to timeout");
            context.setMessage("Connection lost");
            return RECONNECTING;
        }
    },
    
    RECONNECTING {
        @Override
        public ConnectionState connect(Connection context) {
            System.out.println("Attempting to reconnect...");
            context.incrementReconnectAttempts();
            if (context.getReconnectAttempts() > 3) {
                System.out.println("Max reconnection attempts reached");
                return ERROR;
            }
            return CONNECTING;
        }
        
        @Override
        public ConnectionState disconnect(Connection context) {
            System.out.println("Canceling reconnection");
            context.resetReconnectAttempts();
            return DISCONNECTED;
        }
        
        @Override
        public ConnectionState sendData(Connection context, String data) {
            System.out.println("Cannot send data - reconnecting");
            context.bufferData(data);
            return this;
        }
        
        @Override
        public ConnectionState receiveData(Connection context) {
            System.out.println("Cannot receive data - reconnecting");
            return this;
        }
        
        @Override
        public ConnectionState timeout(Connection context) {
            return connect(context);
        }
    },
    
    ERROR {
        @Override
        public ConnectionState connect(Connection context) {
            System.out.println("Connection in error state - please reset");
            return this;
        }
        
        @Override
        public ConnectionState disconnect(Connection context) {
            System.out.println("Clearing error state");
            context.resetReconnectAttempts();
            context.setMessage("Error cleared");
            return DISCONNECTED;
        }
        
        @Override
        public ConnectionState sendData(Connection context, String data) {
            System.out.println("Cannot send data - error state");
            return this;
        }
        
        @Override
        public ConnectionState receiveData(Connection context) {
            System.out.println("Cannot receive data - error state");
            return this;
        }
        
        @Override
        public ConnectionState timeout(Connection context) {
            System.out.println("Error state - no timeout handling");
            return this;
        }
    };
    
    public abstract ConnectionState connect(Connection context);
    public abstract ConnectionState disconnect(Connection context);
    public abstract ConnectionState sendData(Connection context, String data);
    public abstract ConnectionState receiveData(Connection context);
    public abstract ConnectionState timeout(Connection context);
}