
from typing import Any
import cv2
import numpy
import numpy as np
from numpy import dtype, ndarray

colors: list[tuple[int, int, int]] = [(255, 0, 0), (0, 0, 255), (0, 255, 255), (255, 0, 255), (60, 60, 60)]

# copy and paste this script into pipeline SEVEN and click "save"

# CONFIGS
purple_lower: tuple[int, int, int] = (89, 68, 51)
purple_upper: tuple[int, int, int] = (173, 239, 255)
green_lower: tuple[int, int, int] = (48, 102, 64)
green_upper: tuple[int, int, int] = (128, 255, 255)

area_threshold: int = 2500
y_cutoff_lower: int = 800
y_cutoff_upper: int = 170

def get_center_x(contour: ndarray[tuple[Any, ...], dtype[Any]]) -> int:
    x, y, w, h = cv2.boundingRect(contour)
    return int(x + w / 2)

def get_center_y(contour: ndarray[tuple[Any, ...], dtype[Any]]) -> int:
    x, y, w, h = cv2.boundingRect(contour)
    return int(y + h / 2)

def get_contour_center(contour: ndarray[tuple[Any, ...], dtype[Any]]) -> tuple[int, int]:
    return get_center_x(contour), get_center_y(contour)

def filter_contour_from_y(contour: ndarray[tuple[Any, ...], dtype[Any]]) -> bool:
    if y_cutoff_lower != 0:
        contour_y = get_center_y(contour)
        if contour_y > y_cutoff_lower:
            return False

    if y_cutoff_upper != 0:
        contour_y = get_center_y(contour)
        if contour_y < y_cutoff_upper:
            return False

    return True


def find_contours(hsv, lower: tuple[int, int, int], upper: tuple[int, int, int]) -> list[ndarray[tuple[Any, ...], dtype[Any]]]:
    threshold = cv2.inRange(hsv, lower, upper)
    contours_seq, _ = cv2.findContours(threshold, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    return list(contours_seq)

def filter_contours(contours: list[ndarray[tuple[Any, ...], dtype[Any]]]) -> list[ndarray[tuple[Any, ...], dtype[Any]]]:
    if len(contours) == 0:
        return contours
    filtered_area: list = list(filter(lambda contour: cv2.contourArea(contour) > area_threshold, contours))

    filtered_y: list = list(filter(filter_contour_from_y, filtered_area))

    return filtered_y

def draw(contours: list[ndarray[tuple[Any, ...], dtype[Any]]], color: tuple[int, int, int], image) -> None:
    for contour in contours:
        x, y, w, h = cv2.boundingRect(contour)
        center: tuple[int, int] = get_contour_center(contour)
        cv2.rectangle(image, (x, y), (x+w, y+h), color, 5)
        cv2.circle(image, center, 7, (255, 255, 255), -1)

def runPipeline(image: numpy.ndarray, llrobot: list):
    fake_data: list = [0, 320, 320, 640, 640, 960, 960, 1280]

    boxes: list[tuple[int, int]] = []

    for i in range(0, len(fake_data), 2):
        boxes.append((fake_data[i], fake_data[i+1]))

    color_index: int = 0

    img_hsv: numpy.ndarray = cv2.cvtColor(image, cv2.COLOR_BGR2HSV) # convert to hsv

    purpel: list[ndarray[tuple[Any, ...], dtype[Any]]] = filter_contours(find_contours(img_hsv, purple_lower, purple_upper)) # find all contours for purple
    gren: list[ndarray[tuple[Any, ...], dtype[Any]]] = filter_contours(find_contours(img_hsv, green_lower, green_upper)) # find all contours for green
    all_contours: list[ndarray[tuple[Any, ...], dtype[Any]]] = purpel + gren

    llpython = [
        0, # purple detections
        0  # green detections
    ]

    # drawing
    draw(purpel, (128, 0, 128), image)
    draw(gren, (0, 255, 0), image)

    cv2.line(image, (0, y_cutoff_lower), (1280, y_cutoff_lower), (255, 0, 0), 5)
    cv2.line(image, (0, y_cutoff_upper), (1280, y_cutoff_upper), (0, 0, 255), 5)

    llpython[0] = len(purpel)
    llpython[1] = len(gren)

    text_y = 50

    for box in boxes:
        x1, x2 = box
        box_color: tuple[int, int, int] = colors[color_index]
        color_index += 1
        cv2.line(image, (x1, 0), (x1, 960), box_color, 5)
        cv2.line(image, (x2, 0), (x2, 960), box_color, 5)
        artifacts = 0
        for contour in all_contours:
            x, y, w, h = cv2.boundingRect(contour)
            center_x = x + (w / 2)
            is_within = x1 < center_x < x2
            if is_within:
                artifacts += 1

        cv2.putText(image, f"Box {color_index}: {artifacts}", (0, text_y), cv2.FONT_HERSHEY_SIMPLEX, 2, box_color, 5, cv2.LINE_AA)
        text_y += 55
        llpython.append(artifacts)

    return np.array([[]]), image, llpython