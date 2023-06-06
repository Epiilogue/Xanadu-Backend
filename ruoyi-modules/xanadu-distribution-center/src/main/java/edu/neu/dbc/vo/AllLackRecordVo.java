package edu.neu.dbc.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import edu.neu.dbc.entity.Product;
import edu.neu.dbc.vo.SingleLackRecordVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-02 11:12:09
 */
@Getter
@Setter
@TableName("dbc_lack_record")
@ApiModel(value = "LackRecord对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
public class AllLackRecordVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品")
    Product product;

    @ApiModelProperty("当前库存数量")
    private Integer nowCount;

    @ApiModelProperty("缺货数量")
    private Integer needCount;

    @ApiModelProperty("进货数量")
    private Integer inputCount;

    @ApiModelProperty("创建日期")
    private Date createTime;

    @ApiModelProperty("缺货记录")
    private List<SingleLackRecordVo> singleLackRecordVos;


}
