package com.serverapi.communicator;

public interface SuccessListener {
  final static int RESULT_CANCELED = 0;
  final static int RESULT_OK = 1;

  public void onSuccess(int requestCode, int resultCode, Object returnValue);
}
