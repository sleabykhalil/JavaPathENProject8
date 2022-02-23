package tourGuide.helper;

import java.util.Date;

public class DateTimeHelper {
    public DateTimeHelper() {
    }

    public String getTimeStamp(){
        return new Date().toInstant().toString().concat("_").concat(String.valueOf(Math.random()));
    }


}
