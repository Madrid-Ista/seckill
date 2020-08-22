package jh.enums;

/**
 * 使用枚举存储状态字段
 */
public enum SeckillStatEnum {
    SUCCESS(1, "秒杀成功"),
    END(0, "秒杀结束"),
    REPEAT_KILL(-1, "重复秒杀"),
    INNER_ERROR(-2, "系统异常"),
    DATA_REWRITE(-3, "数据篡改");


    private int status;

    private String statusInfo;

    SeckillStatEnum(int status, String statusInfo) {
        this.status = status;
        this.statusInfo = statusInfo;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusInfo() {
        return statusInfo;
    }

    public static SeckillStatEnum statusOf(int index){
        // values()返回enum实例的数组
        for(SeckillStatEnum status : values()){
            if(status.getStatus() == index){
                return status;
            }
        }
        return null;
    }
}
