package mu.zz.pikaso.aliennationmonitor.representation;


public class Character {
    private String Guild;
    private String Name;
    private Integer Level;
    private Integer Resets;
    private String Clas;
    private Integer BansCount;
    private Integer Bonus;
    private String Map;
    private Integer X;
    private Integer Y;
    private String ServerName;
    private String Status;

    public Character(){}

    public Character(String nick, String profa){
        this.Name = nick;
        this.Clas = profa;
    }

    public Character(String nick, String profa, String status){
        this.Name = nick;
        this.Clas = profa;
        this.Status = status;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    private Boolean isLive;

    public String getGuild() {
        return Guild;
    }

    public void setGuild(String guild) {
        Guild = guild;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getLevel() {
        return Level;
    }

    public void setLevel(Integer level) {
        Level = level;
    }

    public Integer getResets() {
        return Resets;
    }

    public void setResets(Integer resets) {
        Resets = resets;
    }

    public String getClas() {
        return Clas;
    }

    public void setClas(String clas) {
        Clas = clas;
    }

    public Integer getBansCount() {
        return BansCount;
    }

    public void setBansCount(Integer bansCount) {
        BansCount = bansCount;
    }

    public Integer getBonus() {
        return Bonus;
    }

    public void setBonus(Integer bonus) {
        Bonus = bonus;
    }

    public String getMap() {
        return Map;
    }

    public void setMap(String map) {
        Map = map;
    }

    public Integer getX() {
        return X;
    }

    public void setX(Integer x) {
        X = x;
    }

    public Integer getY() {
        return Y;
    }

    public void setY(Integer y) {
        Y = y;
    }

    public String getServerName() {
        return ServerName;
    }

    public void setServerName(String serverName) {
        ServerName = serverName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
