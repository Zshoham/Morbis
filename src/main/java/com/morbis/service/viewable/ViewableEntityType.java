package com.morbis.service.viewable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ViewableEntityType {

    GAME,
    LEAGUE,
    SEASON,
    COACH,
    PLAYER,
    REFEREE,
    TEAM_OWNER,
    TEAM_MANAGER,
    STADIUM,
    TEAM;

    public static final List<ViewableEntityType> all = Stream.of(
            GAME,
            LEAGUE,
            SEASON,
            COACH,
            PLAYER,
            REFEREE,
            TEAM_OWNER,
            TEAM_MANAGER,
            STADIUM,
            TEAM).collect(Collectors.toList());

    public static final List<ViewableEntityType> assets = Stream.of(
            COACH,
            PLAYER,
            TEAM_OWNER,
            TEAM_MANAGER,
            STADIUM).collect(Collectors.toList());

    public static final List<ViewableEntityType> pages = Stream.of(
            COACH,
            PLAYER,
            TEAM).collect(Collectors.toList());


    public static final ClassValidator VIEWABLE_VALIDATOR =
            classValidatorFor(all);

    public static final ClassValidator ASSETS_VALIDATOR =
            classValidatorFor(assets);

    public static final ClassValidator PAGES_VALIDATOR =
            classValidatorFor(pages);


    public static MatchBuilder matchAll() {
        return new MatchBuilder(all);
    }

    public static MatchBuilder matchAssets() {
        return new MatchBuilder(assets);
    }

    public static MatchBuilder matchPages() {
        return new MatchBuilder(pages);
    }

    public static MatchBuilder match(ViewableEntityType... filter) { return new MatchBuilder(Stream.of(filter).collect(Collectors.toList())); }

    public static MatchBuilder match(List<ViewableEntityType> filter) { return new MatchBuilder(filter); }

    public static ClassValidator classValidatorFor(ViewableEntityType... types) {
        return new ClassValidator(Stream.of(types).collect(Collectors.toSet()));
    }

    public static ClassValidator classValidatorFor(List<ViewableEntityType> types) {
        return new ClassValidator(new HashSet<>(types));
    }

    public static class MatchBuilder {

        private HashMap<ViewableEntityType, Runnable> searchers;
        private final List<ViewableEntityType> filter;

        private MatchBuilder(List<ViewableEntityType> filter) {
            this.filter = filter;
            searchers = new HashMap<>();
        }

        public MatchBuilder inCase(ViewableEntityType type, Runnable searcher) {
            if (searchers.containsKey(type))
                throw new IllegalArgumentException("cannot match on the same type (" + type + ") twice");

            if (filter.contains(type))
                searchers.put(type, searcher);

            return this;
        }

        public void execute() {
            searchers.values().forEach(Runnable::run);
        }

        public void parallelExecute() { searchers.values().parallelStream().forEach(Runnable::run); }

        public Runnable hold() {
            return () -> searchers.values().forEach(Runnable::run);
        }

        public Runnable parallelHold() { return () -> searchers.values().parallelStream().forEach(Runnable::run); }
    }

    public static class ClassValidator {

        private final Set<ViewableEntityType> types;

        private ClassValidator(Set<ViewableEntityType> types) {
            this.types = types;
        }

        public boolean validate(Class<?> type) {
            return types.stream().map(ViewableEntityType::toString).collect(Collectors.toSet())
                    .contains(classToEnumName(type));
        }

        private String classToEnumName(Class<?> type) {
            StringBuilder res = new StringBuilder();
            String simpleName = type.getSimpleName();

            int lastUpper = 0;

            for (int i = 1; i < simpleName.length(); i++) {
                if (Character.isUpperCase(simpleName.charAt(i))) {
                    res.append(simpleName.substring(lastUpper, i).toUpperCase()).append("_");
                    lastUpper = i;
                }
            }

            res.append(simpleName.substring(lastUpper).toUpperCase());
            return res.toString();
        }
    }
}
