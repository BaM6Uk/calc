package me.vlad.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    TextView input, equal;
    Boolean lastNum, error, lastDot, firstZero, lastRoot;
    Expression expression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Присвоение id и значений переменным
        input = findViewById(R.id.input);
        equal = findViewById(R.id.equal);

        lastNum = false;
        error = false;
        lastDot = false;
        firstZero = true;
        lastRoot = false;
    }

    //Нажатие на кнопку "="
    public void onEqualClick(View view) {
        onEqual();
        String nonequal = equal.getText().toString().replace("=", "");
        if (input.getText().toString().length() > 12) {
            input.setTextSize(24);
            equal.setTextSize(48);
            equal.setTextColor(Color.WHITE);
            input.setTextColor(Color.GRAY);
        }
        if (input.getText().toString().length() > 16) {
            input.setTextSize(20);
            equal.setTextSize(44);
            equal.setTextColor(Color.WHITE);
            input.setTextColor(Color.GRAY);
        }
        if (input.getText().toString().length() > 22) {
            input.setTextSize(12);
            equal.setTextSize(40);
            equal.setTextColor(Color.WHITE);
            input.setTextColor(Color.GRAY);
        }
        if (input.getText().toString().length() <= 13) {
            input.setTextSize(28);
            equal.setTextSize(56);
            equal.setTextColor(Color.WHITE);
            input.setTextColor(Color.GRAY);
        }
    }

    /*public void onPercentClick(View view) {
        double pervalue = Double.parseDouble(input.getText().toString());
        double percentvalue = pervalue / 100;
        equal.setText("=" + String.valueOf(percentvalue));
    }*/

    //Нажатие на на любую цифру
    public void onNumClick(View view) {

        if (input.getText().toString().length() > 12) {
            input.setTextSize(32);
        }
        if (input.getText().toString().length() > 16) {
            input.setTextSize(24);
        }
        if (input.getText().toString().length() > 22) {
            input.setTextSize(16);
        }
        if (firstZero){
            input.setText("");
            firstZero = false;
        }
        if (error){
            //equal.setText("=Ошибка");
            error = false;
            input.setText("0");
        }else {
            Button btn = (Button)view;
            input.append(btn.getText().toString());
        }
        lastNum = true;
        lastRoot = false;
        onEqual();
    }

    //Нажатие на кнопку "A/C"
    public void onFullClearClick(View view) {

        input.setText("0");
        firstZero = true;
        equal.setText("");
        input.setTextSize(40);
        equal.setTextSize(32);
        equal.setTextColor(Color.GRAY);
        input.setTextColor(Color.WHITE);
        lastNum = false;
        error = false;
        lastDot = false;
        equal.setVisibility(View.GONE);
    }

    //Нажатие на кнопку оператора "√", "÷", "×", "-", "+"
    public void onOperatorClick(View view) {

        if (!error && !lastRoot){
            if (firstZero){
                input.setText("");
                firstZero = false;
            }
            Button btn = (Button)view;
            input.append(btn.getText().toString());
            lastDot = false;
            lastNum = false;
            onEqual();
        }
    }
    public void onRootClick(View view) {

        if (!error && !lastRoot){
            if (firstZero){
                input.setText("");
                firstZero = false;
            }
            Button btn = (Button)view;
            input.append(btn.getText().toString());
            lastDot = false;
            lastNum = false;
            lastRoot = true;
            onEqual();
        }
    }

    //Нажатие на кнопку "⌫"
    public void onBackInputClick(View view) {

        String inputtxt = input.getText().toString();
        input.setText(inputtxt.substring(0, inputtxt.length() - 1));


        try{
            String lastChar = inputtxt.substring(inputtxt.length() - 1);

            if (Character.isDigit(lastChar.charAt(0))){
                onEqual();
            }
        }catch (Exception e){
            equal.setText("");
            equal.setVisibility(View.GONE);
            Log.e("last char error", e.toString());
        }
    }

    //Нажатие на кнопку "C"
    public void onClearInputClick(View view) {

        input.setText("0");
        firstZero = true;
        lastNum = false;
        input.setTextSize(40);
        equal.setTextSize(32);
        equal.setTextColor(Color.GRAY);
        input.setTextColor(Color.WHITE);
    }

    //Мат вычисления и отображение в равенстве
    public void onEqual() {

        if (lastNum && !error){
            String text = input.getText().toString();
            if (text.contains("√")) {
                text = input.getText().toString().replace("√", "sqrt");
            }
            if (text.contains("√-")) {
                text = input.getText().toString().replaceAll("√-", "sqrt");
            }
            if (text.contains("×")) {
                text = input.getText().toString().replace("×", "*");
            }
            if (text.contains("÷")) {
                text = input.getText().toString().replace("÷", "/");
            }

            //text = text.replaceAll("÷", "/");// input.getText().toString().replace("÷", "/");
            //text = input.getText().toString().replace("×", "*");
            //text = input.getText().toString().replace("√", "sqrt");
            expression = new ExpressionBuilder(text).build();
            try {
                Double result = expression.evaluate();
                equal.setVisibility(View.VISIBLE);
                equal.setText("=" + result);
            }catch (ArithmeticException ae){
                Log.e("evaluate error", ae.toString());
                equal.setText("=Ошибка");
                error = true;
                lastNum = false;
            }
        }
    }
}