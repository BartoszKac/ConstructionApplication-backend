package com.example.backend.model;
import com.example.backend.constants.COLOR;
import java.util.List;

public class AreaSetRequest {

    private COLOR color;
    private List<Area> areas;

    public AreaSetRequest() {}

    public AreaSetRequest(COLOR color, List<Area> areas) {
        this.color = color;
        this.areas = areas;
    }

    public COLOR getColor() {
        return color;
    }

    public void setColor(COLOR color) {
        this.color = color;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }
}
