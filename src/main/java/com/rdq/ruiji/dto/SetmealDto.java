package com.rdq.ruiji.dto;


import com.rdq.ruiji.entity.Setmeal;
import com.rdq.ruiji.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
