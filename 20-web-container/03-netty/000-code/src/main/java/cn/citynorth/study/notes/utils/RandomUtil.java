package cn.citynorth.study.notes.utils;

import java.util.Random;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan Chen 2021/07/12 15:32
 */
public class RandomUtil {
    private static Random random = new Random();

    /**
     * 按照范围，生成随机的整数
     *
     * @param mod
     * @return [1, mod]之间的整数
     */
    public static int randInMod(int mod) {
        return Math.abs(random.nextInt(mod)) + 1;
    }

    /**
     * 按照上下限范围，生成随机的整数
     *
     * @param low  下限
     * @param high 上限
     * @return
     */
    public static int randInRange(int low, int high) {

        return randInMod(high - low) + low;

    }


}
