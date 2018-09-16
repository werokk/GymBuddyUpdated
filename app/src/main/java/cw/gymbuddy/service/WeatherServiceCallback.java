package cw.gymbuddy.service;

import cw.gymbuddy.data.Channel;

public interface WeatherServiceCallback {

    void serviceSuccess(Channel channel );
    void serviceFailure(Exception exception);


}
