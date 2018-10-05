
public class ConnectN {
    private static int globalID = 0;

    public static final int MIN_WIDTH = 6;
    public static final int MAX_WIDTH = 16;
    public static final int MIN_HEIGHT = 6;
    public static final int MAX_HEIGHT = 16;
    public static final int MIN_N = 4;

    private int width;
    private int height;
    private int N;
    private int id;

    private boolean isStarted = false;

    private Player[][] player2DArray;

    public int getWidth() {
        return this.width;
    }

    public boolean setWidth(int setWidth) {
        if(setWidth >= ConnectN.MIN_WIDTH && setWidth <= ConnectN.MAX_WIDTH && !this.isStarted) {
            this.width = setWidth;
            if(this.height > 0) {
                this.player2DArray = new Player[this.width][this.height];
            }
            if(!this.setN(this.getN())) {
                this.N = 0;
            }
            return true;
        }
        return false;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean setHeight(int setHeight) {
        if(setHeight >= ConnectN.MIN_HEIGHT && setHeight <= ConnectN.MAX_HEIGHT && !this.isStarted) {
            this.height = setHeight;
            if(this.width > 0) {
                this.player2DArray = new Player[this.width][this.height];
            }
            if(!this.setN(this.getN())) {
                this.N = 0;
            }
            return true;
        }
        return false;
    }

    public int getN() {
        return this.N;
    }

    public boolean setN(int newN) {
        int max = this.width > this.height ? this.width : this.height - 1;
        if(this.width > 0 && this.height > 0 && newN >= 4 && newN <= max && !this.isStarted) {
            this.N = newN;
            return true;
        }
        return false;
    }

    public int getID() {
        return this.id;
    }

    public static int getTotalGames() {
        return ConnectN.globalID;
    }

    public ConnectN(int setWidth, int setHeight, int setN) {
        if(setWidth >= ConnectN.MIN_WIDTH && setWidth <= ConnectN.MAX_WIDTH ) {
            this.width = setWidth;
        }
        if(setHeight >= ConnectN.MIN_HEIGHT && setHeight <= ConnectN.MAX_HEIGHT) {
            this.height = setHeight;
        }
        this.setN(setN);
        id = ConnectN.globalID++;
        if(this.width > 0 && this.height > 0) {
            this.player2DArray = new Player[this.width][this.height];
        }
    }

    public ConnectN() {

    }

    public ConnectN(int setWidth, int setHeight) {
        this(setWidth, setHeight, 0);
    }

    public ConnectN(ConnectN otherBoard) {
        this(otherBoard.getWidth(), otherBoard.getHeight(), otherBoard.getN());
    }

    public boolean setBoardAt(Player player, int setX, int setY) {
        if(this.width == 0 || this.height == 0 || this.N == 0) {
            return false;
        }
        if(player == null) {
            return false;
        }
        if(setX < 0 || setX >= this.width || setY < 0 || setY >= this.height ) {
            return false;
        }
        if( getBoardAt(setX, setY) != null || getBoardAt(setX, setY - 1) == null) {
            return false;
        }
        if (this.isEnded()) {
            return false;
        }
        if(player2DArray != null) {
            player2DArray[setX][setY] = player;
            this.isStarted = true;
            return true;
        } else {
            return false;
        }
    }

    private boolean isEnded() {

    }

    public boolean setBoardAt(Player player, int setX) {
        for(int y = 0; y < this.height; y++) {
            if(this.getBoardAt(setX, y) == null) {
                return this.setBoardAt(player, setX, y);
            }
        }
        return false;
    }

    public Player getBoardAt(int getX, int getY) {
        if (getX < 0 || getX >= this.width || getY < 0 || getY >= this.height || !this.isStarted) {
            return null;
        }
        if(player2DArray != null) {
            return player2DArray[getX][getY];
        } else {
            return null;
        }
    }

    public Player[][] getBoard() {
        if(this.width > 0 && this.height > 0) {
            Player[][] players = new Player[this.width][this.height];
            for(int i = 0; i < this.player2DArray.length; i++) {
                for(int j = 0; j < this.player2DArray[i].length; j++) {
                    players[i][j] = this.player2DArray[i][j];
                }
            }
            return players;
        } else {
            return null;
        }
    }

    public Player getWinner() {

    }

    public static ConnectN create(int width, int height, int n) {
        ConnectN connect = new ConnectN(width, height, n);
        if(connect.getWidth() > 0 && connect.getHeight() > 0 && connect.getN() > 0) {
            return connect;
        } else {
            return null;
        }
    }

    public static ConnectN[] createMany(int number, int width, int height, int n) {
        ConnectN connect = ConnectN.create(width, height, n);
        if(connect != null) {
            ConnectN[] connects = new ConnectN[number];
            connects[0] = connect;
            for(int i = 1; i < number; i++) {
                connects[i] = ConnectN.create(width, height, n);
            }
            return connects;
        } else {
            return null;
        }
    }

    public static boolean compareBoards(ConnectN firstBoard, ConnectN secondBoard) {
        if(firstBoard.getWidth() == secondBoard.getWidth() && firstBoard.getHeight() == secondBoard.getHeight() && firstBoard.getN() == secondBoard.getN()) {
            for(int i = 0; i < firstBoard.player2DArray.length; i++) {
                for(int j = 0; j < firstBoard.player2DArray[i].length; j++) {
                    if(!firstBoard.player2DArray[i][j].equals(secondBoard.player2DArray[i][j])) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean compareBoards(ConnectN... boards) {
        for(int i = 0; i < boards.length - 1; i++) {
            if(!compareBoards(boards[i], boards[i + 1])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConnectN other = (ConnectN) obj;
        return id == other.id;
    }
}
