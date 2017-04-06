import { exec, SuccessCallback, ErrorCallback } from '../utils';

export class Analytics {
  private static _instance: Analytics;

  constructor() {
    // Prevent creating multiple instances
    if (Analytics._instance) {
      return Analytics._instance;
    }
  }

  static getInstance(): Analytics {
    if (!this._instance) {
      this._instance = new Analytics();
    }

    return this._instance;
  }

  logEvent(name: string, params: LogEventParams): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'analytics_logEvent', [name, params]);
    });
  }

  setScreenName(name: string): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'analytics_setScreenName', [name]);
    });
  }

  setUserId(id: string): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'analytics_setUserId', [id]);
    });
  }

  setUserProperty(name: string, value: string): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'analytics_setUserProperty', [name, value]);
    });
  }

}

export interface LogEventParams {
  [k: string]: any;
}

export const analytics = () => Analytics.getInstance();
