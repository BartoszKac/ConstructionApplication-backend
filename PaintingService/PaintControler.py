import cv2
import numpy as np
import tkinter as tk  # Do pobrania rozdzielczości ekranu
from Aialgorythm import SAM_Painter


class PaintMyRoomApp:
    def __init__(self, image_path):
        self.img = cv2.imread(image_path)
        if self.img is None: raise Exception("Błąd wczytywania zdjęcia!")

        self.h, self.w = self.img.shape[:2]
        self.current_display = self.img.copy()

        # Pobieramy wymiary monitora
        root = tk.Tk()
        self.screen_width = root.winfo_screenwidth()
        self.screen_height = root.winfo_screenheight()
        root.destroy()

        self.ai_painter = SAM_Painter()
        self.ai_painter.prepare_image(self.img)

    def center_window(self, window_name):
        # Obliczamy środek
        # Jeśli zdjęcie jest większe niż ekran, warto je przeskalować do wyświetlania
        win_w = self.w
        win_h = self.h

        # Jeśli zdjęcie jest za duże na ekran, OpenCV i tak je przytnie,
        # więc wyśrodkowanie liczymy względem wymiarów okna
        pos_x = int((self.screen_width - win_w) / 2)
        pos_y = int((self.screen_height - win_h) / 2)

        # Przesuwamy okno
        cv2.moveWindow(window_name, pos_x, pos_y)

    def run(self):
        win_name = "PaintMyRoom - Ultra Fast AI"
        cv2.namedWindow(win_name)

        # Wywołujemy centrowanie
        self.center_window(win_name)

        cv2.setMouseCallback(win_name, self.on_click)

        while True:
            cv2.imshow(win_name, self.current_display)
            key = cv2.waitKey(1) & 0xFF
            if key == ord('q'):
                break
            if key == ord('c'):
                self.current_display = self.img.copy()

        cv2.destroyAllWindows()

    def on_click(self, event, x, y, flags, param):
        if event == cv2.EVENT_LBUTTONDOWN:
            mask = self.ai_painter.segment([y, x], self.h, self.w)
            if mask is not None:
                self.apply_paint(mask, [0, 0, 150])  # Czerwony w BGR

    def apply_paint(self, mask, color):
        alpha = 0.45
        roi = self.current_display[mask]
        paint = np.full_like(roi, color)
        self.current_display[mask] = cv2.addWeighted(roi, 1 - alpha, paint, alpha, 0)


if __name__ == "__main__":
    import sys

    path = sys.argv[1] if len(sys.argv) > 1 else "tlo2.jpg"
    app = PaintMyRoomApp(path)
    app.run()