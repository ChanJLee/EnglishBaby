package com.chan.babytextviewedit.utils;

import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TextJustification {

    /**
     * 调整显示的内容
     * @param input 将要显示的数据
     * @param textView 要显示内容的view
     * @param contentWidth 内容宽度
     * @return 调整后的内容
     */
    public static String justify(String input, TextView textView, float contentWidth) {

        String tempText;
        String resultText = "";

        Paint paint = textView.getPaint();

        //先分段
        List<String> paraList = paraBreak(input);

        //遍历每个段内容
        for (int i = 0; i < paraList.size(); i++) {

            List<String> lineList = lineBreak(paraList.get(i).trim(), paint, contentWidth);
            tempText = TextUtils.join(" ", lineList).replaceFirst("\\s*", "");
            resultText += tempText.replaceFirst("\\s*", "") + "\n";
        }

        return resultText;
    }

    /**
     * 分段
     * @param text
     * @return
     */
    private static List<String> paraBreak(String text) {

        //以换行符 换
        ArrayList<String> paraList = new ArrayList<String>();
        String[] paraArray = text.split("\\n+");
        for (String para : paraArray) {
            paraList.add(para);
        }
        return paraList;
    }

    /**
     * 拆分单词
     * @param text 一行的数据
     * @param paint 用于测量文字大小
     * @param contentWidth 每行的宽度
     * @return 每行的单词内容
     */
    private static List<String> lineBreak(String text, Paint paint, float contentWidth) {

        //想了下 这里的contentWidth还是要保留的 防止用户误调用api
        //内容是通过text view的getWidth 这要严格遵循控件的周期

        //以空白分隔内容
        String[] wordArray = text.split("\\s");

        ArrayList<String> lineList = new ArrayList<String>();
        String myText = "";

        for (String word : wordArray) {

            //如果 当前的一行可以放得下
            //那么就把它放下
            if (paint.measureText(myText + " " + word) <= contentWidth)
                myText = myText + " " + word;
            else {

                int totalSpacesToInsert = (int) ((contentWidth - paint.measureText(myText)) / paint.measureText(" "));
                lineList.add(justifyLine(myText, totalSpacesToInsert));
                myText = word;
            }
        }

        lineList.add(myText);
        return lineList;
    }

    /**
     * 填充一行
     * @param text 一行的内容
     * @param totalSpacesToInsert 要填充的空格数
     * @return 最终的单词
     */
    private static String justifyLine(String text, int totalSpacesToInsert) {
        String[] wordArray = text.split("\\s");
        String toAppend = " ";


        //看下每个单词要平分多少个空格
        while ((totalSpacesToInsert) >= (wordArray.length - 1)) {
            toAppend = toAppend + " ";
            totalSpacesToInsert -= (wordArray.length - 1);
        }

        int i = 0;
        String justifiedText = "";
        for (String word : wordArray) {
            if (i < totalSpacesToInsert)
                justifiedText = justifiedText + word + " " + toAppend;

            else
                justifiedText = justifiedText + word + toAppend;

            i++;
        }

        return justifiedText;
    }


}