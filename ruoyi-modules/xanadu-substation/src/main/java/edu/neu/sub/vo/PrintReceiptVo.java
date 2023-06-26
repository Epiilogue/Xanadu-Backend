package edu.neu.sub.vo;

import edu.neu.sub.entity.Substation;
import edu.neu.sub.entity.Task;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Api("打印收据")
public class PrintReceiptVo implements Serializable {
    Task task;
    Substation substation;
}
