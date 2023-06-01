package edu.neu.cc.serviceImpl;

import edu.neu.dto.OperationDto;
import edu.neu.mapper.OperationDtoMapper;
import edu.neu.service.OperationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gaosong Xu
 * @since 2023-06-01 10:32:09
 */
@Service
public class OperationServiceImpl extends ServiceImpl<OperationDtoMapper, OperationDto> implements OperationService {

}
