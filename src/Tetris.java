import java.awt.event.KeyEvent;

/**
 * Created by ree-natt on 25.03.17.
 */
public class Tetris {
    private Field field;
    private Figure figure;
    public static Tetris game;
    private boolean isGameOver;

    public Tetris(int width, int height)
    {
        field = new Field(width, height);
        figure = null;
    }

    public static void main(String[] args) throws Exception {
        game = new Tetris(10, 20);
        game.run();
    }

    public void run() throws Exception {
        //Создаем объект "наблюдатель за клавиатурой" и стартуем его.
        KeyboardObserver keyboardObserver = new KeyboardObserver();
        keyboardObserver.start();

        //выставляем начальное значение переменной "игра окончена" в ЛОЖЬ
        isGameOver = false;
        //создаем первую фигурку посередине сверху: x - половина ширины, y - 0.
        figure = FigureFactory.createRandomFigure(field.getWidth() / 2, 0);

        //пока игра не окончена
        while (!isGameOver)
        {
            //"наблюдатель" содержит события о нажатии клавиш?
            if (keyboardObserver.hasKeyEvents())
            {
                //получить самое первое событие из очереди
                KeyEvent event = keyboardObserver.getEventFromTop();
                //Если равно символу 'q' - выйти из игры.
                if (event.getKeyChar() == 'q') return;
                //Если "стрелка влево" - сдвинуть фигурку влево
                if (event.getKeyCode() == KeyEvent.VK_LEFT)
                    figure.left();
                    //Если "стрелка вправо" - сдвинуть фигурку вправо
                else if (event.getKeyCode() ==  KeyEvent.VK_RIGHT)
                    figure.right();
                    //Если  код клавишы равен 12 ("цифра 5 на доп. клавиатуре") - повернуть фигурку
                else if (event.getKeyCode() ==  12)
                    figure.rotate();
                    //Если "пробел" - фигурка падает вниз на максимум
                else if (event.getKeyCode() ==  KeyEvent.VK_SPACE)
                    figure.downMaximum();
            }

            step();             //делаем очередной шаг
            field.print();      //печатаем состояние "поля"
            Thread.sleep(800);  //пауза 300 миллисекунд - 1/3 секунды
        }

        //Выводим сообщение "Game Over"
        System.out.println("Game Over");
    }

    public void step() {
        figure.down();
        if (!figure.isCurrentPositionAviable()) {
            figure.up();
            figure.landed();

            isGameOver = figure.getY() <= 1;

            field.removeFullLines();

            figure = FigureFactory.createRandomFigure(field.getWidth() / 2, 0);
        }
    }

    public Field getField() {
        return field;
    }

    public Figure getFigure() {
        return figure;
    }
}
