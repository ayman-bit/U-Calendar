/*
 *  Copyright (C) 2017 Dirk Lemmermann Software & Consulting (dlsc.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package impl.com.calendarfx.view;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DayEntryView;
import com.calendarfx.view.DraggedEntry;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The default day entry view.
 * It displays a title and the start time of the entry.
 */
public class DayEntryViewSkin extends SkinBase<DayEntryView> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    protected Label startTimeLabel;
    protected Label titleLabel;

    private final InvalidationListener updateStylesListener = it -> updateStyles();
    private final WeakInvalidationListener weakUpdateStylesListener = new WeakInvalidationListener(updateStylesListener);

    private final InvalidationListener updateLabelsListener = it -> updateLabels();
    private final WeakInvalidationListener weakUpdateLabelsListener = new WeakInvalidationListener(updateLabelsListener);

    public DayEntryViewSkin(DayEntryView view) {
        super(view);

        startTimeLabel = createStartTimeLabel();
        startTimeLabel.setManaged(false);
        startTimeLabel.setMouseTransparent(true);

        titleLabel = createTitleLabel();
        titleLabel.setManaged(false);
        titleLabel.setMouseTransparent(true);

        getChildren().addAll(startTimeLabel, titleLabel);

        Entry<?> entry = getEntry();

        entry.intervalProperty().addListener(weakUpdateLabelsListener);
        entry.calendarProperty().addListener(weakUpdateStylesListener);
        entry.titleProperty().addListener(weakUpdateLabelsListener);

        getSkinnable().positionProperty().addListener(weakUpdateLabelsListener);
        updateLabels();

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(view.widthProperty());
        clip.heightProperty().bind(view.heightProperty());
        view.setClip(clip);

        view.nodesProperty().addListener((Observable it) -> updateNodes());
        updateStyles();
        updateNodes();
    }

    private void updateNodes() {
        final DayEntryView view = getSkinnable();
        final ObservableMap<Pos, List<Node>> nodes = view.getNodes();

        if (nodes != null) {
            Set<Pos> previouslyUsedPositions = null;
            if (nodePanes != null) {
                previouslyUsedPositions = nodePanes.keySet();
            }

            final Set<Pos> currentlyUsedPositions = nodes.keySet();

            // we have to remove some previously used flow panes from the entry view
            if (previouslyUsedPositions != null) {
                previouslyUsedPositions.removeAll(currentlyUsedPositions);
                final Iterator<Pos> iterator = previouslyUsedPositions.iterator();
                while (iterator.hasNext()) {
                    final Pos next = iterator.next();
                    final FlowPane removedPane = nodePanes.remove(next);
                    getChildren().remove(removedPane);
                }
            }

            currentlyUsedPositions.forEach(pos -> createPosition(pos, nodes.get(pos)));
        }

        if (nodePanes != null && nodePanes.isEmpty()) {
            nodePanes = null;
        }
    }

    private Map<Pos, FlowPane> nodePanes;

    private void createPosition(Pos pos, List<Node> nodes) {
        if (nodePanes == null) {
            nodePanes = new HashMap<>();
        }

        FlowPane flowPane = nodePanes.computeIfAbsent(pos, p -> {
            FlowPane pane = new FlowPane();
            pane.getStyleClass().add("icon-flow-pane");
            pane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            pane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            pane.setManaged(false);
            pane.setMouseTransparent(true);

            switch (pos) {
                case TOP_CENTER:
                case CENTER:
                case BOTTOM_RIGHT:
                case BASELINE_LEFT:
                case BASELINE_CENTER:
                case BOTTOM_LEFT:
                case BOTTOM_CENTER:
                case BASELINE_RIGHT:
                    pane.setOrientation(Orientation.HORIZONTAL);
                    pane.prefWrapLengthProperty().bind(getSkinnable().widthProperty());
                    break;
                case TOP_LEFT:
                case TOP_RIGHT:
                case CENTER_LEFT:
                case CENTER_RIGHT:
                    pane.setOrientation(Orientation.VERTICAL);
                    pane.prefWrapLengthProperty().bind(getSkinnable().heightProperty());
                    break;
            }

            pane.setAlignment(pos);

            getChildren().add(pane);
            return pane;
        });

        flowPane.getChildren().setAll(nodes);
    }

    /**
     * @return The entry.
     */
    protected Entry<?> getEntry() {
        Entry<?> entry = getSkinnable().getEntry();
        if (entry.isRecurrence()) {
            entry = entry.getRecurrenceSourceEntry();
        }
        return entry;
    }

    /**
     * This methods updates the styles of the node according to the entry
     * settings.
     */
    protected void updateStyles() {
        DayEntryView view = getSkinnable();
        Entry<?> entry = getEntry();

        Calendar calendar = entry.getCalendar();
        if (entry instanceof DraggedEntry) {
            calendar = ((DraggedEntry) entry).getOriginalCalendar();
        }

        // when the entry gets removed from its calendar then the calendar can
        // be null
        if (calendar == null) {
            return;
        }

        view.getStyleClass().setAll("default-style-entry", calendar.getStyle() + "-entry");

        if (entry.isRecurrence()) {
            view.getStyleClass().add("recurrence");
        }

        view.getStyleClass().addAll(entry.getStyleClass());

        startTimeLabel.getStyleClass().setAll("start-time-label", "default-style-entry-time-label", calendar.getStyle() + "-entry-time-label");
        titleLabel.getStyleClass().setAll("title-label", "default-style-entry-title-label", calendar.getStyle() + "-entry-title-label");
    }

    /**
     * The label used to show the start time.
     *
     * @return The label component.
     */
    protected Label createStartTimeLabel() {
        Label label = new Label();
        label.setMinSize(0, 0);

        return label;
    }

    /**
     * Convert the given time to a string.
     *
     * @param time the time
     * @return The formatted time.
     */
    protected String formatTime(LocalTime time) {
        return formatter.format(time);
    }

    /**
     * Convert the given title. This method can be overridden for e.g.
     * translating the title.
     *
     * @param title the title
     * @return The formatted title.
     */
    protected String formatTitle(String title) {
        return title;
    }

    /**
     * The label used to show the title.
     *
     * @return The title component.
     */
    protected Label createTitleLabel() {
        Label label = new Label();
        label.setWrapText(true);
        label.setMinSize(0, 0);

        return label;
    }

    /**
     * This method will be called if the labels need to be updated.
     */
    protected void updateLabels() {
        Entry<?> entry = getEntry();

        startTimeLabel.setText(formatTime(entry.getStartTime()));
        titleLabel.setText(formatTitle(entry.getTitle()));
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        // title label
        double titleHeight = titleLabel.prefHeight(contentWidth);

        // it is guaranteed that we have enough height to display the title (see
        // "computeMinHeight")
        titleLabel.resizeRelocate(snapPosition(contentX), snapPosition(contentY), snapSize(contentWidth), snapSize(titleHeight));

        // start time label
        double timeLabelHeight = startTimeLabel.prefHeight(contentWidth);
        if (contentHeight - titleHeight > timeLabelHeight) {
            startTimeLabel.setVisible(true);
            startTimeLabel.resizeRelocate(snapPosition(contentX), snapPosition(contentY + titleHeight), snapSize(contentWidth), snapSize(timeLabelHeight));
        } else {
            startTimeLabel.setVisible(false);
        }

        if (nodePanes != null) {
            nodePanes.keySet().forEach(pos -> layoutNodesPane(pos, nodePanes.get(pos), contentX, contentY, contentWidth, contentHeight));
        }
    }

    private void layoutNodesPane(Pos pos, FlowPane flowPane, double x, double y, double w, double h) {
        double pw = flowPane.prefWidth(-1);
        double ph = flowPane.prefHeight(-1);

        switch (pos) {
            case TOP_LEFT:
                flowPane.resizeRelocate(x, y, pw, ph);
                break;
            case TOP_CENTER:
                flowPane.resizeRelocate(x + w / 2 - pw / 2, y, pw, ph);
                break;
            case TOP_RIGHT:
                flowPane.resizeRelocate(x + w - pw, y, pw, ph);
                break;
            case BOTTOM_LEFT:
            case BASELINE_LEFT:
                flowPane.resizeRelocate(x, h - ph, pw, ph);
                break;
            case BOTTOM_CENTER:
            case BASELINE_CENTER:
                flowPane.resizeRelocate(x + w / 2 - pw / 2, h - ph, pw, ph);
                break;
            case BOTTOM_RIGHT:
            case BASELINE_RIGHT:
                flowPane.resizeRelocate(x + w - pw, h - ph, pw, ph);
                break;
            case CENTER_LEFT:
                flowPane.resizeRelocate(x, y + h / 2 - ph / 2, pw, ph);
                break;
            case CENTER:
                flowPane.resizeRelocate(x + w / 2 - pw / 2, y + h / 2 - ph / 2, pw, ph);
                break;
            case CENTER_RIGHT:
                flowPane.resizeRelocate(x + w - pw, y + h / 2 - ph / 2, pw, ph);
                break;
        }
    }

    @Override
    protected double computeMinHeight(double width, double topInset,
                                      double rightInset, double bottomInset, double leftInset) {
        if (titleLabel != null && getSkinnable().isMinHeightEqualToTitleHeight()) {
            // For this pref height calculation we do not consider the available
            // width because
            // we only want to show a single line of text anyways.
            return titleLabel.prefHeight(-1) + topInset + bottomInset;
        }

        return super.computeMinHeight(width, topInset, rightInset, bottomInset, leftInset);
    }
}
