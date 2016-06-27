/*
 * Copyright (C) 2011-2016 clueminer.org
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
package org.clueminer.sort;

import java.util.Arrays;
import java.util.Random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author deric
 */
public class MaxHeapInvTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testSort() {
        Integer[] data = new Integer[10];
        MaxHeapInv<Integer> heap = new MaxHeapInv<>(data);
        int size = 10;
        for (int i = 0; i < size; i++) {
            heap.add(size - i);
        }
        System.out.println("data: " + Arrays.toString(data));
        //heapify should guarantee that smallest element is first
        //heap.heapify();
        heap.print();
        assertEquals(1, data[0].intValue());
        assertEquals(10, data[9].intValue());

        assertEquals(1, heap.peek().intValue());
        assertEquals(10, heap.peekLast().intValue());

        heap.print();
        arrPrint(data);
    }

    //@Test
    public void testUnSorted() {
        int size = 5;
        Integer[] data = new Integer[size];
        MaxHeapInv<Integer> heap = new MaxHeapInv<>(data);
        for (int i = 0; i < size; i++) {
            heap.add(999999);
        }

        heap.add(1);
        heap.add(5);
        //heap.print();
        heap.add(3);
        heap.add(8);
        heap.add(4);
        System.out.println("==== 8 should be last");
        heap.heapify();
        heap.print();
        heap.sort();
        assertEquals(8, heap.peekLast().intValue());
        heap.print();

        heap.add(2);

        heap.sort();
        arrPrint(data);
        //smallest value should be at position 0
        assertEquals(1, data[0].intValue());
    }

    private void print(MaxHeapInv<Integer> heap, int n) {
        for (int i = 0; i < n; i++) {
            System.out.println(i + ": " + heap.get(i));
        }
    }

    /**
     * we use heap for sorting an array
     *
     * @param heap
     */
    private void arrPrint(Integer[] heap) {
        for (int i = 0; i < heap.length; i++) {
            System.out.println("[" + i + "] = " + heap[i]);
        }
    }

    //@Test
    public void testDoubleSorted() {
        int size = 5;
        Double[] data = new Double[size];
        MaxHeapInv<Double> heap = new MaxHeapInv<>(data);
        for (int i = 0; i < size; i++) {
            heap.add(Double.MAX_VALUE);
        }
        heap.add(0.35);
        heap.add(1.35);
        heap.add(0.5);
        heap.add(0.1);
        heap.add(0.01);
        heap.print();
        heap.sort();
        assertEquals(0.01, heap.peek(), DELTA);
    }

    //@Test
    public void testRandom() {
        int size = 20;
        Double[] data = new Double[size];
        Random rand = new Random();
        MaxHeapInv<Double> heap = new MaxHeapInv<>(data);
        for (int i = 0; i < size; i++) {
            heap.add(rand.nextDouble() * 10);
        }
        heap.heapify();
        for (int i = 0; i < size; i++) {
            heap.add(rand.nextDouble() * 10);
            heap.heapify();
        }
        double prev = 0.0;
        for (double d : heap) {
            assertTrue("expect " + prev + " to be smaller than " + d, prev < d);
        }
    }

}
