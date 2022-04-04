package com.infoecos.cn.destiny.lib.预测.utils;

import com.infoecos.cn.destiny.utils.Arrays2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MathUtil {

    //    private static final float _1sigma = .6526f;
//    private static final float _2sigma = .9544f;
//    private static final float _3sigma = .9974f;
    private static final int _maxSum = 7;

    static float getStandardDeviation(float[] weights) {
        return getDeviation(weights, getAverage(weights));
    }

    static float getDeviation(float[] weights, float average) {
        float deviation = 0;
        for (float w : weights) {
            deviation += (w - average) * (w - average);
        }
        deviation /= weights.length;

        return (float) Math.sqrt(deviation);
    }

    static Integer[][] sigmaIdxUponDeviation(float[] weights) {
        return sigmaIdxUponDeviation(weights, getAverage(weights));
    }

    static Integer[][] sigmaIdxUponDeviation(float[] weights, float average) {
        float deviation = getDeviation(weights, average);
        Integer[][] sigmaIdx = new Integer[5][];
        List<Integer> s = new ArrayList<>();
        for (int i = 0; i < weights.length; ++i) {
            if (average - weights[i] >= 2 * deviation) s.add(i);
        }
        sigmaIdx[0] = s.toArray(new Integer[s.size()]);

        s = new ArrayList<>();
        for (int i = 0; i < weights.length; ++i) {
            if (average - weights[i] < 2 * deviation && average - weights[i] >= deviation) s.add(i);
        }
        sigmaIdx[1] = s.toArray(new Integer[s.size()]);

        s = new ArrayList<>();
        for (int i = 0; i < weights.length; ++i) {
            if (average - weights[i] < deviation && weights[i] - average < deviation) s.add(i);
        }
        sigmaIdx[2] = s.toArray(new Integer[s.size()]);

        s = new ArrayList<>();
        for (int i = 0; i < weights.length; ++i) {
            if (weights[i] - average < 2 * deviation && weights[i] - average >= deviation) s.add(i);
        }
        sigmaIdx[3] = s.toArray(new Integer[s.size()]);

        s = new ArrayList<>();
        for (int i = 0; i < weights.length; ++i) {
            if (weights[i] - average >= 2 * deviation) s.add(i);
        }
        sigmaIdx[4] = s.toArray(new Integer[s.size()]);

        return sigmaIdx;
    }

    static float getSum(float[] weights) {
        float sum = 0;
        for (float w : weights) {
            sum += w;
        }

        return sum;
    }

    static float getAverage(float[] weights) {
        return getSum(weights) / weights.length;
    }

    static int[] getOverflow(float[] weights) {
        float[] newWeights = Arrays2.copyOf(weights, weights.length);
        float deviation = getStandardDeviation(newWeights), average = getAverage(newWeights);
        List<Integer> exts = new ArrayList<Integer>();
        for (int i = 1; i < newWeights.length; ++i) {
            if (Math.abs(newWeights[i - 1] - average) < deviation
                    && Math.abs(newWeights[i] - average) < deviation)
                continue;
            if (Math.max(newWeights[i], newWeights[i - 1])
                    / Math.min(newWeights[i], newWeights[i - 1]) < 2)
                continue;
            exts.add(i);
            // if (Math.abs(newWeights[i] - average) < deviation)
            // continue;
            //
            // if ((newWeights[i - 1] < newWeights[i] && newWeights[i] >
            // newWeights[i + 1])
            // || (newWeights[i - 1] > newWeights[i] && newWeights[i] <
            // newWeights[i + 1]))
            // exts.add(i);
        }

        int[] ret = new int[exts.size()];
        for (int i = exts.size() - 1; i >= 0; --i) {
            ret[i] = exts.get(i);
        }
        return ret;
    }

    static int[] getTops(float[] weights) {
        float[] newWeights = Arrays2.copyOf(weights, weights.length);
        for (int i = 0; i < newWeights.length; ++i) {
            if (newWeights[i] > 2)
                newWeights[i] = 2;
        }
        float deviation = getDeviation(newWeights, 1), valve = (deviation > .5f) ? .5f
                : deviation;
        Set<Integer> tops = new HashSet<Integer>();
        for (int i = 1; i < newWeights.length - 1; ++i) {
            if (newWeights[i - 1] < newWeights[i]
                    && newWeights[i] > newWeights[i + 1]) {
                if (Math.abs(newWeights[i] - 1) > valve)
                    continue;
                tops.add(i);
            }
        }

        float[] sort = Arrays2.copyOf(weights, weights.length);
        for (int i = sort.length - 1; i > 0; --i) {
            float max = sort[i];
            for (int j = 0; j < i; ++j) {
                if (max < sort[j]) {
                    max = sort[j];
                    sort[j] = sort[i];
                    sort[i] = max;
                }
            }
        }
        for (int x = sort.length - 1, sum = 0; x >= 0 && sum < _maxSum; --x) {
            if (sort[x] == 2)
                continue;
            for (int i = 1; i < weights.length - 1; ++i) {
                if (sort[x] == weights[i]) {
                    if (weights[i - 1] < weights[i]
                            && weights[i] > weights[i + 1]) {
                        tops.add(i);
                        ++sum;
                        break;
                    }
                }
            }
        }

        // for (int i = 0; i < newWeights.length; ++i) {
        // if (newWeights[i] < (1 + deviation))
        // newWeights[i] = 2;
        // }
        // valve = getDeviation(newWeights, 2);
        // for (int i = 1; i < newWeights.length - 1; ++i) {
        // if (newWeights[i] == 2)
        // continue;
        // if ((newWeights[i - 1] == 2 || newWeights[i - 1] < newWeights[i])
        // && (newWeights[i] > newWeights[i + 1] || newWeights[i + 1] == 2)) {
        // if (Math.abs(newWeights[i] - 2) > valve)
        // continue;
        // tops.add(i);
        // }
        // }

        return sort(tops);
    }

    static int[] getBottoms(float[] weights) {
        float[] newWeights = Arrays2.copyOf(weights, weights.length);
        for (int i = 0; i < newWeights.length; ++i) {
            if (newWeights[i] > 2)
                newWeights[i] = 2;
        }
        float deviation = getDeviation(newWeights, 1), valve = (deviation > .5f) ? .5f
                : deviation;
        Set<Integer> bottoms = new HashSet<Integer>();
        for (int i = 1; i < newWeights.length - 1; ++i) {
            if (newWeights[i - 1] > newWeights[i]
                    && newWeights[i] < newWeights[i + 1]) {
                if (Math.abs(newWeights[i] - 1) > valve)
                    continue;
                bottoms.add(i);
            }
        }

        float[] sort = Arrays2.copyOf(weights, weights.length);
        for (int i = sort.length - 1; i > 0; --i) {
            float min = sort[i];
            for (int j = 0; j < i; ++j) {
                if (min > sort[j]) {
                    min = sort[j];
                    sort[j] = sort[i];
                    sort[i] = min;
                }
            }
        }
        for (int x = sort.length - 1, sum = 0; x >= 0 && sum < _maxSum; --x) {
            if (sort[x] == 0)
                continue;
            for (int i = 1; i < weights.length - 1; ++i) {
                if (sort[x] == weights[i]) {
                    if (weights[i - 1] > weights[i]
                            && weights[i] < weights[i + 1]) {
                        bottoms.add(i);
                        ++sum;
                        break;
                    }
                }
            }
        }

        // for (int i = 0; i < newWeights.length; ++i) {
        // if (newWeights[i] > (1 - deviation))
        // newWeights[i] = 0;
        // }
        // valve = getDeviation(newWeights, 0);
        // for (int i = 1; i < newWeights.length - 1; ++i) {
        // if (newWeights[i] == 0)
        // continue;
        // if ((newWeights[i - 1] == 0 || newWeights[i - 1] > newWeights[i])
        // && (newWeights[i] < newWeights[i + 1] || newWeights[i + 1] == 0)) {
        // if (Math.abs(newWeights[i] - 0) > valve)
        // continue;
        // bottoms.add(i);
        // }
        // }

        return sort(bottoms);
    }

    private static int[] sort(Set<Integer> arr) {
        Integer[] ar = arr.toArray(new Integer[arr.size()]);
        int[] ret = new int[ar.length];
        for (int i = ar.length - 1; i >= 0; --i) {
            int id = i;
            for (int j = 0; j < i; ++j) {
                if (ar[j] > ar[id])
                    id = j;
            }
            ret[i] = ar[id];
            ar[id] = ar[i];
        }
        return ret;
    }
}
