package alik.leverxfinalproject.constants;

import alik.leverxfinalproject.entity.GameObject;
import alik.leverxfinalproject.model.GameObjectDTO;

public class MapObjects {
    public static GameObjectDTO mapToDTO(GameObject gameObject) {
        GameObjectDTO dto = new GameObjectDTO();
        dto.setId(gameObject.getId());
        dto.setName(gameObject.getTitle());
        dto.setText(gameObject.getText());
        dto.setGameId(gameObject.getGame().getId());
        dto.setUserId(gameObject.getAppUser().getId());
        return dto;
    }
}
