package com.benwest496.sudokusolver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private GridLayout mSudokuGridLayout;
    private GridLayout mButtonsGridLayout;
    private GridLayout mNumbersGridLayout;
    private int selectID;
    private Button mResetButton;
    private Button mClearButton;
    private Button mSolveButton;
    private Button mOneButton;
    private Button mTwoButton;
    private Button mThreeButton;
    private Button mFourButton;
    private Button mFiveButton;
    private Button mSixButton;
    private Button mSevenButton;
    private Button mEightButton;
    private Button mNineButton;
    private List<Button> numberButtons = new ArrayList<>();
    private List<GridSquare> gridSquares;
    private List<Integer> possibleVals = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
    private List<Integer> ids = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridSquares = new ArrayList<>();
        mNumbersGridLayout = (GridLayout) findViewById(R.id.numbersGridLayout);
        mButtonsGridLayout = (GridLayout) findViewById(R.id.buttonsGridLayout);
        mSudokuGridLayout = (GridLayout)findViewById(R.id.sudokuGridLayout);
        mSudokuGridLayout.removeAllViews();
        setGridLayout(mSudokuGridLayout, 9, 9, 3.75, 5.2);
        mResetButton = (Button) findViewById(R.id.resetButton);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < 81; i++){
                    TextView tv = (TextView) findViewById(i);
                    tv.setText(" ");
                }
                for (int j = 0; j < gridSquares.size(); j++){
                    GridSquare gs = gridSquares.get(j);
                    gs.setPossibles(Arrays.asList(1,2,3,4,5,6,7,8,9));
                }
            }
        });
        mClearButton = (Button) findViewById(R.id.clearButton);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectID == 81){
                    Toast.makeText(MainActivity.this, "Please select a square!", Toast.LENGTH_SHORT).show();
                }
                else {
                    TextView selectTV = (TextView) findViewById(selectID);
                    selectTV.setText(" ");
                }
            }
        });
        mSolveButton = (Button) findViewById(R.id.solveButton);
        mSolveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solve(gridSquares);
            }
        });
        mOneButton = (Button)findViewById(R.id.oneTextView);
        numberButtons.add(mOneButton);
        mTwoButton = (Button)findViewById(R.id.twoTextView);
        numberButtons.add(mTwoButton);
        mThreeButton = (Button)findViewById(R.id.threeTextView);
        numberButtons.add(mThreeButton);
        mFourButton = (Button)findViewById(R.id.fourTextView);
        numberButtons.add(mFourButton);
        mFiveButton = (Button)findViewById(R.id.fiveTextView);
        numberButtons.add(mFiveButton);
        mSixButton = (Button)findViewById(R.id.sixTextView);
        numberButtons.add(mSixButton);
        mSevenButton = (Button)findViewById(R.id.sevenTextView);
        numberButtons.add(mSevenButton);
        mEightButton = (Button)findViewById(R.id.eightTextView);
        numberButtons.add(mEightButton);
        mNineButton = (Button)findViewById(R.id.nineTextView);
        numberButtons.add(mNineButton);
        for(Button b : numberButtons) {
            final Button finB = b;
            finB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNumber(finB);
                }
            });
        }
    }

    private void setGridLayout(GridLayout gridLayout, int row, int column, double width, double height){
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row + 1);
        for(int i =0, c = 0, r = 0; i < row*column; i++, c++)
        {
            if(c == column)
            {
                c = 0;
                r++;
            }
            final TextView mTextView = new TextView(this);
            int id = (9*r)+c;
            ids.add(id);
            GridSquare gs = new GridSquare((char)(64+r), c, id, possibleVals);
            gridSquares.add(gs);
            //System.out.println("Id = "+id);
            mTextView.setId(id);
            mTextView.setPadding(10,10,10,10);
            mTextView.setWidth((int) (dpWidth/width));
            mTextView.setHeight((int) (dpHeight/height));
            mTextView.setBackgroundResource(R.drawable.gsback);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i = 0; i < 81; i++) {
                        TextView view = (TextView) findViewById(i);
                        view.setBackgroundResource(R.drawable.gsback);
                    }
                    mTextView.setBackgroundResource(R.drawable.cellselect);
                    selectID = mTextView.getId();
                    System.out.println(mTextView.getId());
                }
            });

            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            param.rightMargin = 5;
            param.topMargin = 5;
            param.leftMargin = 5;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            mTextView.setLayoutParams (param);
            gridLayout.addView(mTextView);
            mTextView.setText(" ");
        }
    }

    private void setNumber(Button button){
        String buttonVal = (String) button.getText();
        TextView tv = (TextView) findViewById(selectID);
        tv.setText(buttonVal);
        GridSquare gs = getGridSquareByID(selectID);
        gs.setPossibles(Arrays.asList(Integer.parseInt(buttonVal)));
        //System.out.println("Id = "+gs.getId() + " Number of possibles = " + gs.getPossibles().size());
        updateColumnPossibles(gs);
    }

    private void solve(List<GridSquare> squares){
        for(int i = 0; i < squares.size(); i++){
            GridSquare gs = squares.get(i);
            int id = gs.getId();
            List<Integer> possibles = gs.getPossibles();
            System.out.println("Id is "+ id + " Num possibles is: " + possibles.size());
            TextView tv = (TextView) findViewById(id);
            if(tv.getText().equals(" ")) {
                int randFill = selectRandomPossible(possibles);
                tv.setText(randFill + "");
            }
        }
    }

    private void updateColumnPossibles(GridSquare gs){
        List<GridSquare> sameCol = new ArrayList<>();
        int colIndex = gs.getColIndex();
        List<Integer> gsPossible = gs.getPossibles();
        int gsVal = gsPossible.get(0);
        for(int i = 0; i < gridSquares.size(); i++){
            GridSquare gsComp = gridSquares.get(i);
            if(gsComp.getColIndex() == colIndex && gsComp.getId() != gs.getId()){
                sameCol.add(gsComp);
                System.out.println("Size of same col = "+sameCol.size());
            }
        }
        for (int k = 0; k < sameCol.size(); k++){
            GridSquare gs1 = sameCol.get(k);
            System.out.println("Removing from "+ gs1.getRowIndex()+" "+gs1.getColIndex());
            gs1.getPossibles().remove(Integer.valueOf(gsVal));
        }

    }

    private int selectRandomPossible(List<Integer> possibles){
        Random r = new Random();
        int randPoss = r.nextInt(possibles.size());
        int randFill = possibles.get(randPoss);
        return randFill;
    }

    private GridSquare getGridSquareByID(int id) {
        int sameGridValIndex = 0;
        for (int i = 0; i < gridSquares.size(); i++) {
            GridSquare gs = gridSquares.get(i);
            if (gs.getId() == id) {
                sameGridValIndex = i;
            }
        }
        return gridSquares.get(sameGridValIndex);
    }
}
