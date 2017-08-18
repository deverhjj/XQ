package com.biu.modulebase.binfenjiari.widget.video_player_manager.player_messages;

/**
 * This generic interface for messages
 */
public interface Message {
    void runMessage();
    void polledFromQueue();
    void messageFinished();
}
