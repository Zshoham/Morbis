package com.morbis.service;

import com.morbis.model.league.repository.LeagueRepository;
import com.morbis.model.league.repository.SeasonRepository;
import com.morbis.model.member.repository.RefereeRepository;

public class AssociationRepService {
    private SeasonRepository seasonRepository;
    private LeagueRepository leagueRepository;
    private RefereeRepository refereeRepository;

    public AssociationRepService(SeasonRepository seasonRepository, LeagueRepository leagueRepository, RefereeRepository refereeRepository) {
        this.seasonRepository = seasonRepository;
        this.leagueRepository = leagueRepository;
        this.refereeRepository = refereeRepository;
    }
}
