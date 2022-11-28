package com.moshui.reggie.dto;

import com.moshui.reggie.entity.Setmeal;
import com.moshui.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
