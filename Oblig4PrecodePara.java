import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;

/**
 * Klasse for aa tegne et punktsett med n punkter (Helst n < 200) og
 * den konvekse innhyllinga i IntList CoHull, koordinatene i d.x[] og d.y[]
 * ver 7.mai 2015, 2016,2017
 * Modified 27.04.20
 **/

class Oblig4PrecodePara extends JFrame {
    ConvexPara d;
    IntList theCoHull;
    int n;
    int[] x, y;
    Graph grafen;
    int size, margin;
    double scale;

    Oblig4PrecodePara(ConvexPara d, IntList CoHull) {
        theCoHull = CoHull;
        this.d = d;
        x = d.x;
        y = d.y;
        n = d.n;
        size = 100; // will probably need adjusting depending on your n and seed in NPunkter17
        margin = 200; // will probably need adjusting depending on your n and seed in NPunkter17
        scale = size / x[d.MAX_X] + 0.8;
    }

    public void drawGraph() {
        setTitle("Oblig4, num points:" + n);
        grafen = new Graph();
        getContentPane().add(grafen, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        // angir foretrukket storrelse paa dette lerretet.
        setPreferredSize(new Dimension(x[d.MAX_X] + 2 * margin, y[d.MAX_Y] + 2 * margin));
    }

    public void writeHullPoints() {
        String filename = "CONVEX-HULL-POINTS_" + n + ".txt";

        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.printf(
                    "Found %d number of convex hull points in a graph with n = %d:\n______________________________________________________\n\n",
                    this.theCoHull.size(), n);

            for (int i = 0; i < this.theCoHull.size(); i++) {
                writer.print("(" + x[this.theCoHull.get(i)] + "," + y[this.theCoHull.get(i)] + ")");
            }

            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.printf("Got exception when trying to write file %s : ", filename, e.getMessage());
        }
    }

    class Graph extends JPanel {
        void drawPoint(int p, Graphics g) {
            int SIZE = 7;
            if (n <= 50)
                g.drawString(p + "(" + x[p] + "," + y[p] + ")", xDraw(x[p]) - SIZE / 2, yDraw(y[p]) - SIZE / 2);
            else if (n <= 200)
                g.drawString(p + "", xDraw(x[p]) - SIZE / 2, yDraw(y[p]) - SIZE / 2);
            g.drawOval(xDraw(x[p]) - SIZE / 2, yDraw(y[p]) - SIZE / 2, SIZE, SIZE);
            g.fillOval(xDraw(x[p]) - SIZE / 2, yDraw(y[p]) - SIZE / 2, SIZE, SIZE);
        }

        Graph() {
            setPreferredSize(new Dimension(size + 2 * margin + 10, size + 2 * margin + 10));
        }

        int xDraw(int xValue) {
            return (int) (xValue * scale) + margin;
        }

        int yDraw(int yValue) {
            return (int) ((y[d.MAX_Y] - yValue) * scale + margin);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            for (int i = 0; i < n; i++) {
                drawPoint(i, g);
            }
            g.setColor(Color.red);
            // draw cohull
            int x2 = x[theCoHull.get(0)], y2 = y[theCoHull.get(0)], x1, y1;
            for (int i = 1; i < theCoHull.size(); i++) {
                y1 = y2;
                x1 = x2;
                x2 = x[theCoHull.get(i)];
                y2 = y[theCoHull.get(i)];
                g.drawLine(xDraw(x1), yDraw(y1), xDraw(x2), yDraw(y2));
            }

            g.drawLine(xDraw(x[theCoHull.get(theCoHull.size() - 1)]),
                    yDraw(y[theCoHull.get(theCoHull.size() - 1)]),
                    xDraw(x[theCoHull.get(0)]), yDraw(y[theCoHull.get(0)]));
        } // end paintComponent

    } // end class Graph
}// end class DT