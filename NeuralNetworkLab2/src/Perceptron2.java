import java.io.*;
import java.util.Arrays;

public class Perceptron2 {
    private double[] x; // Входные данные
    private double y; // Значение нейрона
    private double[][] wxh; // Веса от 1 слоя до 2
    private double[] why; // Веса от 2 слоя до значения нейрона
    private double[] h; // Значения 2 слоя
    //double[][] pat = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } }; // Паттерн входных данных
    private double[] r = { 0, 1 }; // Паттерн выходных данных
    private double[][] pat1; // Новый паттерн входных значений
    private double[][] pat2; // Паттерн для тестирования
    private int size_VMK3 = 0; // Количество данных в файле VMK3
    private int size_VMK5 = 0; // Количество данных в файле VMK5
    private int size_PHY3 = 0; // Количество данных в файле PHY3
    private int size_PHY5 = 0; // Количество данных в файле PHY5
    private File file_VMK3 = new File("src\\VMK3.txt"); // Объект, хранящий файл VMK3 (обучение)
    private File file_VMK5 = new File("src\\VMK5.txt"); // Объект, хранящий файл VMK5 (обучение)
    private File file_PHY3 = new File("src\\PHYS3.txt"); // Объект, хранящий файл PHY3 (тестирование)
    private File file_PHY5 = new File("src\\PHYS5.txt"); // Объект, хранящий файл PHY5 (тестирование)
    private int count3 = 0;
    private int count5 = 0;
    private int count3_2 = 0;
    private int count5_2 = 0;
    private double[][] kxh; // Приращение для пересчёта весов от x до h
    private double[] khy; // Приращение для пересчёта весов от h до y


    // Значение f(x) = 1 / (1 + e^(-1))
    public double func(double x) {
        return 1 / (1 + Math.exp((-1) * x));
    }

    public void test() {
        int countRight = 0;
        int countAll = 0;

        for (int p = 0; p < pat2.length; p++) {
            for (int i = 0; i < x.length; i++)
                x[i] = pat2[p][i];
            if (p <= count3_2) {
                if (y == 0) {
                    countRight++;
                    countAll++;
                } else {
                    countAll++;
                }
            } else {
                if (y == 1) {
                    countRight++;
                    countAll++;
                } else {
                    countAll++;
                }
            }

        }

        System.out.println("\n" + (double) countRight / countAll);
    }

    public void readDataTest() {
        try {
            // Определение способы считывания для файлов
            FileReader fileReader_PHY3 = new FileReader(file_PHY3);
            BufferedReader bufferedReader_PHY3 = new BufferedReader(fileReader_PHY3);
            FileReader fileReader_PHY5 = new FileReader(file_PHY5);
            BufferedReader bufferedReader_PHY5 = new BufferedReader(fileReader_PHY5);

            // Определение количества данных в PHY3
            String nextLine = bufferedReader_PHY3.readLine();
            while (nextLine != null) {
                size_PHY3++;
                nextLine = bufferedReader_PHY3.readLine();
            }

            // Определение количества данных в PHY5
            nextLine = bufferedReader_PHY5.readLine();
            while (nextLine != null) {
                size_PHY5++;
                nextLine = bufferedReader_PHY5.readLine();
            }
            // Создание массива для хранения данных
            pat2 = new double[size_PHY3 + size_PHY5][5];

            fileReader_PHY3 = new FileReader(file_PHY3);
            bufferedReader_PHY3 = new BufferedReader(fileReader_PHY3);
            fileReader_PHY5 = new FileReader(file_PHY5);
            bufferedReader_PHY5 = new BufferedReader(fileReader_PHY5);

            // Считывание файла PHY3
            String line[];
            nextLine = bufferedReader_PHY3.readLine();
            while (nextLine != null) {
                line = nextLine.split(";"); // Можно заполнить и через for
                pat2[count3_2][0] = Double.parseDouble(line[0]);
                pat2[count3_2][1] = Double.parseDouble(line[1]);
                pat2[count3_2][2] = Double.parseDouble(line[2]);
                pat2[count3_2][3] = Double.parseDouble(line[3]);
                pat2[count3_2][4] = Double.parseDouble(line[4]);
                count3_2++;
                nextLine = bufferedReader_PHY3.readLine();
            }
            count5_2 = count3_2;
            count3_2--;

            // Считывание файла PHY5
            nextLine = bufferedReader_PHY5.readLine();
            while (nextLine != null) {
                line = nextLine.split(";"); // Можно заполнить и через for
                pat2[count5_2][0] = Double.parseDouble(line[0]);
                pat2[count5_2][1] = Double.parseDouble(line[1]);
                pat2[count5_2][2] = Double.parseDouble(line[2]);
                pat2[count5_2][3] = Double.parseDouble(line[3]);
                pat2[count5_2][4] = Double.parseDouble(line[4]);
                count5_2++;
                nextLine = bufferedReader_PHY5.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Считывание данных (обучение)
    public void readData() {
        try {
            // Определение способы считывания для файлов
            FileReader fileReader_VMK3 = new FileReader(file_VMK3);
            BufferedReader bufferedReader_VMK3 = new BufferedReader(fileReader_VMK3);
            FileReader fileReader_VMK5 = new FileReader(file_VMK5);
            BufferedReader bufferedReader_VMK5 = new BufferedReader(fileReader_VMK5);

            // Определение количества данных в VMK3
            String nextLine = bufferedReader_VMK3.readLine();
            while (nextLine != null) {
                size_VMK3++;
                nextLine = bufferedReader_VMK3.readLine();
            }

            // Определение количества данных в VMK5
            nextLine = bufferedReader_VMK5.readLine();
            while (nextLine != null) {
                size_VMK5++;
                nextLine = bufferedReader_VMK5.readLine();
            }
            // Создание массива для хранения данных
            pat1 = new double[size_VMK3 + size_VMK5][5];

            fileReader_VMK3 = new FileReader(file_VMK3);
            bufferedReader_VMK3 = new BufferedReader(fileReader_VMK3);
            fileReader_VMK5 = new FileReader(file_VMK5);
            bufferedReader_VMK5 = new BufferedReader(fileReader_VMK5);

            // Считывание файла VMK3
            String line[];
            nextLine = bufferedReader_VMK3.readLine();
            while (nextLine != null) {
                line = nextLine.split(";"); // Можно заполнить и через for
                pat1[count3][0] = Double.parseDouble(line[0]);
                pat1[count3][1] = Double.parseDouble(line[1]);
                pat1[count3][2] = Double.parseDouble(line[2]);
                pat1[count3][3] = Double.parseDouble(line[3]);
                pat1[count3][4] = Double.parseDouble(line[4]);
                count3++;
                nextLine = bufferedReader_VMK3.readLine();
            }
            count5 = count3;
            count3--;

            // Считывание файла VMK5
            nextLine = bufferedReader_VMK5.readLine();
            while (nextLine != null) {
                line = nextLine.split(";"); // Можно заполнить и через for
                pat1[count5][0] = Double.parseDouble(line[0]);
                pat1[count5][1] = Double.parseDouble(line[1]);
                pat1[count5][2] = Double.parseDouble(line[2]);
                pat1[count5][3] = Double.parseDouble(line[3]);
                pat1[count5][4] = Double.parseDouble(line[4]);
                count5++;
                nextLine = bufferedReader_VMK5.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Конструктор (запускающий)
    public Perceptron2() {
        readData();
        readDataTest();
        x = new double[pat1[0].length]; // Определяем количество входных данных
        h = new double[2]; // Второй слой
        wxh = new double[x.length][h.length]; // Веса из x в h
        why = new double[h.length]; // Веса из h в y
        kxh = new double[x.length][h.length]; // Приращение для пересчёта весов от x до h
        khy = new double[h.length]; // Приращение для пересчёта весов от h до y
        init();
        study();
        test();

        /*
        for (int p = 0; p < pat1.length; p++) {
            for (int i = 0; i < x.length; i++)
                x[i] = pat1[p][i];
            countY();
            System.out.println("y=" + y);
        }*/
    }

    // Инициализация весов
    public void init(){
        System.out.println("------------------------------------------------");
        System.out.println("Начальные значения весов первого слоя");
        for (int i = 0; i < wxh.length; i++) {
            for (int j = 0; j < wxh[i].length; j++) {
                wxh[i][j] = Math.random() * 0.3 + 0.1;
                System.out.println("wxh["+i+"]["+j+"] = " + wxh[i][j]);
            }
        }
        System.out.println("------------------------------------------------");
        System.out.println("Начальные значения весов второго слоя");
        for (int i = 0; i < why.length; i++) {
            why[i] = Math.random() * 0.3 + 0.1;
            System.out.println("why["+i+"] = " + why[i]);
        }
        System.out.println("------------------------------------------------");
    }

    // Вычисления значения в нейроне
    public void countY() {
        for (int i = 0; i < h.length; i++) {
            h[i] = 0;
            for (int j = 0; j < x.length; j++) {
                h[i] += x[j] * wxh[j][i];
            }
            if (h[i] > 0.5)
                h[i] = 1;
            else
                h[i] = 0;
        }
        y = 0;
        for (int i = 0; i < h.length; i++) {
            y += h[i] * why[i];
        }

        if (y > 0.5)
            y = 1;
        else
            y = 0;
    }

    public void study() {
        double globalError;
        int era = 0;
        do {
            globalError = 0;
            for (int p = 0; p < pat1.length; p++) {
                for (int i = 0; i < x.length; i++)
                    x[i] = pat1[p][i];
                countY();
                double lEr;
                if (p <= count3) {
                    lEr = r[0] - y;
                } else {
                    lEr = r[1] - y;
                }
                globalError += Math.abs(lEr);

                double[] h_12 = new double[2];
                double[] sum_h_12 = new double[2];
                Arrays.fill(sum_h_12, 0);
                // Мы знаем, что во 2 слое всего 2 вершины, в ином случае - добавляем внутренний цикл, а h0... в массив
                for (int i = 0; i < x.length; i++) {
                    sum_h_12[0] += wxh[i][0] * x[i];
                    sum_h_12[1] += wxh[i][1] * x[i];
                }
                h_12[0] = func(sum_h_12[0]);
                h_12[1] = func(sum_h_12[1]);

                // Высчитывание приращений
                for (int i = 0; i < h.length; i++)
                    //error[i] = lEr * why[i];
                    khy[i] = (func(why[0] + why[1]) - y) * func(why[0] + why[1]) *
                            (1 - func(why[0] + why[1])) * h_12[i];

                // Высчитывание приращений
                for (int i = 0; i < x.length; i++){
                    for (int j = 0; j < h.length; j++){
                        //wxh[i][j] += 0.1* error[j] * x[i];
                        kxh[i][j] = khy[j] * func(sum_h_12[j]) * (1 - func(sum_h_12[j])) * x[i];
                    }
                }

                // Перерасчёт весов xh
                for (int i = 0; i < x.length; i++) {
                    for (int j = 0; j < h.length; j++) {
                        wxh[i][j] -= 0.1 * kxh[i][j];
                    }
                }

                // Перерасчёт весов hy
                for (int i = 0; i < h.length; i++)
                    why[i] -= 0.1 * khy[i];
            }
            era++;
            System.out.println(era);
            //System.out.println(wxh[0][0]);
            //System.out.println("Era: " + era +
             //       "   global error: " + globalError);
        } while (globalError != 0 && era < 10000000);
    }

    public static void main(String[] args) {
        Perceptron2 perceptron2 = new Perceptron2();
    }
}