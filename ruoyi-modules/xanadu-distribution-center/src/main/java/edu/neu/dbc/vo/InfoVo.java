package edu.neu.dbc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class TempProductVo implements Serializable{
    String Name;
    Double Price;
    String ImgURL;
    Date Date;
    String KeyWord;
    String ShopName;
    String Comment;
}
@Data

public class InfoVo implements Serializable {
    Integer code;
    List<TempProductVo> list;

}