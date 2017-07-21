package design2;

/**
 * 具体的建造者
 * 持有对房子的引用
 * Created by Administrator on 2017/7/6.
 */

public class WorkerBuild  {
    Room room = new Room();
    private RoomParam param = new RoomParam();
    public WorkerBuild makeWindown(String windown) {
        param.windown = windown;
        return  this;
    }

    public WorkerBuild makeFloor(String floor) {
        param.floor = floor;
        return this;
    }

    public Room build() {
        room.apply(param);
        return room;
    }

    class RoomParam{
        public  String windown ;
        public  String floor ;

    }
}
