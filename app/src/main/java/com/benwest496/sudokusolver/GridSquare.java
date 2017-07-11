package com.benwest496.sudokusolver;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

/**
 * Created by Ben on 21/06/2017.
 */

public class GridSquare {
    private int mColIndex;
    private int mId;
    private char mRowIndex;
    private List<Integer> mPossibles;

    public List<Integer> getPossibles() {
        return mPossibles;
    }

    public void setPossibles(List<Integer> possibles) {
        mPossibles = possibles;
    }

    public GridSquare(char rowIndex, int colIndex, int id, List<Integer> possibles){
        mRowIndex = rowIndex;
        mColIndex = colIndex;
        mId = id;
        mPossibles = possibles;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 31); // two randomly chosen prime numbers
        // if deriving: appendSuper(super.hashCode()).
        builder.append(mRowIndex);
        builder.append(mColIndex);
        return builder.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GridSquare other = (GridSquare) obj;
        return new EqualsBuilder().
                // if deriving: appendSuper(super.equals(obj)).
                        append(mRowIndex, other.mRowIndex).
                        append(mColIndex, other.mColIndex).
                        isEquals();
    }

    public int getRowIndex(){
        return mRowIndex;
    }

    public void setRowIndex(char rowNum){
        mRowIndex = rowNum;
    }

    public int getColIndex(){
        return mColIndex;
    }

    public void setColIndex(int colVal){
        mColIndex = colVal;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }
}
