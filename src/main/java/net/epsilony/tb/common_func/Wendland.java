/*
 * Copyright (C) 2013 Man YUAN <epsilon@epsilony.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.epsilony.tb.common_func;

import java.util.Arrays;
import net.epsilony.tb.MiscellaneousUtils;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
//***** with python script to generate some code at the end of this file<----------------------

/**
 *
 * @author <a href="mailto:epsilonyuan@gmail.com">Man YUAN</a>
 */
public class Wendland implements RadialBasisCore {

    private static final double[] WENDLAND_3_1 = new double[]{1, 0, -10, 20, -15, 4};//(-r + 1)**4*(4*r + 1)
    private static final double[] WENDLAND_3_2 = new double[]{3, 0, -28, 0, 210, -448, 420, -192, 35};//(-r + 1)**6*(35*r**2 + 18*r + 3)
    private static final double[] WENDLAND_3_3 = new double[]{1, 0, -11, 0, 66, 0, -462, 1056, -1155, 704, -231, 32};//(-r + 1)**8*(32*r**3 + 25*r**2 + 8*r + 1)
    private static final double[][] WENDLAND_COEFFS_ARRAY = new double[][]{WENDLAND_3_1, WENDLAND_3_2, WENDLAND_3_3};
    private static final int[] LEGAL_CONTINUOUS_ORDERS = new int[]{2, 4, 6};
    int diffOrder;
    PolynomialFunction pFunc;
    PolynomialFunction pFuncDiff;
    private final int continuous;

    public Wendland(int continuous) {
        this.continuous = continuous;
        initWendland(continuous);
    }

    public Wendland() {
        this(4);
    }

    private void initWendland(int continuous) {
        int index = -1;
        for (int i = 0; i < LEGAL_CONTINUOUS_ORDERS.length; i++) {
            int lc = LEGAL_CONTINUOUS_ORDERS[i];
            if (lc == continuous) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            throw new IllegalArgumentException(
                    "The legal continuous order should be one of "
                    + Arrays.toString(LEGAL_CONTINUOUS_ORDERS) + " , not " + continuous);
        }

        pFunc = new PolynomialFunction(WENDLAND_COEFFS_ARRAY[index]);
        pFuncDiff = pFunc.polynomialDerivative();
        diffOrder = 0;
    }

    @Override
    public double[] valuesByDistance(double distance, double[] results) {
        if (distance < 0) {
            throw new IllegalArgumentException("distance should be non-negative, not " + distance);
        }
        if (null == results) {
            results = new double[diffOrder + 1];
        }
        if (distance >= 1) {
            Arrays.fill(results, 0);
            return results;
        }
        results[0] = pFunc.value(distance);
        if (diffOrder >= 1) {
            results[1] = pFuncDiff.value(distance);
        }
        return results;
    }

    @Override
    public double[] valuesByDistanceSquare(double distanceSquare, double[] results) {
        double distance = Math.sqrt(distanceSquare);
        results = valuesByDistance(distance, results);
        if (diffOrder >= 1) {
            results[1] /= 2 * distance;
        }
        return results;
    }

    @Override
    public int getDiffOrder() {
        return diffOrder;
    }

    @Override
    public void setDiffOrder(int diffOrder) {
        if (diffOrder < 0 || diffOrder > 1) {
            throw new IllegalArgumentException("only support diff order 0 or 1, not " + diffOrder);
        }
        this.diffOrder = diffOrder;
    }

    @Override
    public String toString() {
        return MiscellaneousUtils.simpleToString(this) + '{' + "continuous=" + continuous + '}';
    }
}
//python script:
//# -*- coding: utf-8 -*-
//'''
//Created on 2013年8月13日
//
//@author: epsilonyuan@gmail.com
//'''
//
//from __future__ import print_function
//from sympy import *
//
//if __name__ == "__main__":
//    r, u = symbols("r,u")
//    
//    prefix = "private static final"
//    if prefix is not None and len(prefix)>0 and prefix[-1] != " ":
//        prefix += " "
//    
//    wendlands = {
//    "wendland_3_1" : (1 - r) ** 4 * (4 * r + 1),
//    "wendland_3_2" : (1 - r) ** 6 * (35 * r ** 2 + 18 * r + 3),
//    "wendland_3_3" : (1 - r) ** 8 * (32 * r ** 3 + 25 * r ** 2 + 8 * r + 1)
//    }
//    
//    wend_polys = {}
//    for k, v in wendlands.items():
//        wend_polys[k] = v.as_poly()
//        
//    pairs = [(k, v) for k, v in wend_polys.items()]
//    pairs.sort(key=lambda s:s[0])
//    
//    for k, v in pairs:
//        java_coefs = v.all_coeffs()
//        java_coefs.reverse()
//        c = str(java_coefs)
//        c = "{" + c[1:-1] + "}"
//        print(prefix, "double[] ", k.upper(), " = new double[]", c, ";//", wendlands[k], sep="")
//    
//    s = prefix + "double[][] WENDLAND_COEFFS_ARRAY = new double[][]{"
//    for k, v in pairs:
//        s +=k.upper() + ", "
//    s = s[:-2]
//    s += "};"
//    print(s)

