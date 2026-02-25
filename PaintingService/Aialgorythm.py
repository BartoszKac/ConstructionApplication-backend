import cv2
import numpy as np
import torch
from segment_anything import sam_model_registry, SamPredictor


class SAM_Painter:
    def __init__(self, checkpoint_path="sam_vit_b_01ec64.pth"):
        self.device = "cuda" if torch.cuda.is_available() else "cpu"
        print(f"BŁYSKAWICZNY START na: {self.device}")

        # Ładowanie modelu vit_b
        self.sam = sam_model_registry["vit_b"](checkpoint=checkpoint_path)
        self.sam.to(device=self.device)
        self.predictor = SamPredictor(self.sam)
        self.image_set = False
        self.scale = 1.0

    def prepare_image(self, image_bgr):
        h, w = image_bgr.shape[:2]
        self.scale = 640 / max(h, w)
        temp_img = cv2.resize(image_bgr, (0, 0), fx=self.scale, fy=self.scale)
        image_rgb = cv2.cvtColor(temp_img, cv2.COLOR_BGR2RGB)
        self.predictor.set_image(image_rgb)
        self.image_set = True

    def segment(self, seed_point, original_h, original_w):
        if not self.image_set:
            return None
        input_point = np.array([[int(seed_point[1] * self.scale), int(seed_point[0] * self.scale)]])
        input_label = np.array([1])
        masks, scores, _ = self.predictor.predict(
            point_coords=input_point,
            point_labels=input_label,
            multimask_output=False
        )
        full_mask = cv2.resize(masks[0].astype(np.uint8), (original_w, original_h), interpolation=cv2.INTER_NEAREST)
        return full_mask.astype(bool)