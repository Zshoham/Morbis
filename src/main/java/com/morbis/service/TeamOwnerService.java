package com.morbis.service;

import com.morbis.model.member.repository.TeamManagerRepository;
import com.morbis.model.member.repository.TeamOwnerRepository;
import com.morbis.model.team.repository.TeamRepository;

public class TeamOwnerService {
    private TeamOwnerRepository teamOwnerRepository;
    private TeamManagerRepository teamManagerRepository;
    private TeamRepository teamRepository;

    public TeamOwnerService(TeamOwnerRepository teamOwnerRepository, TeamManagerRepository teamManagerRepository, TeamRepository teamRepository) {
        this.teamOwnerRepository = teamOwnerRepository;
        this.teamManagerRepository = teamManagerRepository;
        this.teamRepository = teamRepository;
    }
}
