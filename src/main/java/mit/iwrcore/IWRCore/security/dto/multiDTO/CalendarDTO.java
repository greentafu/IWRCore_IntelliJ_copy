package mit.iwrcore.IWRCore.security.dto.multiDTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CalendarDTO {
    private String title;
    private String startDate;
    private String endDate;
    private int color;
    private String url;

    public CalendarDTO(String title, String startDate, String endDate, int color, String url){
        this.title=title;
        this.startDate=startDate;
        this.endDate=endDate;
        this.color=color;
        this.url=url;
    }
}
