package design2;

/**
 * Created by Administrator on 2017/7/6.
 */

public class Room {
    private String windown ;
    private String floor ;

   public Room apply(WorkerBuild.RoomParam param){
       windown = param.windown;
       floor = param.floor ;
       return this;
   }

    @Override
    public String toString() {
        return super.toString();
    }
}
