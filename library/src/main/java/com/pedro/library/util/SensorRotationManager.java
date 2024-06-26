/*
 * Copyright (C) 2023 pedroSG94.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pedro.library.util;

import android.content.Context;
import android.hardware.SensorManager;
import android.view.OrientationEventListener;

public class SensorRotationManager {

  private final OrientationEventListener listener;
  private int currentOrientation = -1;

  public interface RotationChangedListener {
    void onRotationChanged(int rotation);
  }

  public SensorRotationManager(Context context, boolean avoidDuplicated, final RotationChangedListener rotationListener) {
    this.listener = new OrientationEventListener(context, SensorManager.SENSOR_DELAY_NORMAL) {
      @Override
      public void onOrientationChanged(int sensorOrientation) {
        final int rotation = ((sensorOrientation + 45) / 90) % 4;
        final int rotationDegrees = rotation * 90;
        if (avoidDuplicated) {
          if (currentOrientation == rotationDegrees) return;
          currentOrientation = rotationDegrees;
        }
        rotationListener.onRotationChanged(rotationDegrees);
      }
    };
  }

  public void start() {
    if (listener.canDetectOrientation()) {
      currentOrientation = -1;
      listener.enable();
    }
  }

  public void stop() {
    listener.disable();
    currentOrientation = -1;
  }
}