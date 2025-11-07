package cake.backend.catalog.model.dto;

import java.util.List;

public record SearchDto(String name, List<String> keyWords) {
}
