package edu.neu.dpc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DispatchMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    Long orderId;
    Long substationId;
}
