// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt

package com.google.appinventor.components.runtime;

import com.google.appinventor.components.annotations.SimpleObject;

/**
 * Base class for all non-visible components.
 *
 * @author lizlooney@google.com (Liz Looney)
 */
@SimpleObject
public abstract class AndroidNonvisibleCallbackComponent implements Component {

  protected final Form form;

  /**
   * Creates a new AndroidNonvisibleComponent.
   *
   * @param form the container that this component will be placed in
   */
  protected AndroidNonvisibleCallbackComponent(Form form) {
    this.form = form;
  }

  // Component implementation

  @Override
  public HandlesEventDispatching getDispatchDelegate() {
    return form;
  }

  public void digitalRead1() {
	// TODO Auto-generated method stub
	
  }
}
