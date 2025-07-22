package com.bu.startup.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class GameServerDTO {

    private String serverName;
    private String executablePath;
    private String defaultParams;
}
