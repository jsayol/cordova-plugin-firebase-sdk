import { SuccessCallback, ErrorCallback } from '../utils';
import { App } from '../app';

export class Analytics {
  private static _instance: Analytics;

  private constructor(private _app: App) {
  }

  static getInstance(app: App): Analytics {
    if (!this._instance) {
      this._instance = new Analytics(app);
    }
    return this._instance;
  }

  private _exec(success: SuccessCallback, error: ErrorCallback, action: string, args: any[]) {
    this._app._execWithoutName(success, error, `analytics_${action}`, args);
  }

  logEvent(name: string, params: LogEventParams): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'logEvent', [name, params]);
    });
  }

  setScreenName(name: string): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'setScreenName', [name]);
    });
  }

  setUserId(id: string): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'setUserId', [id]);
    });
  }

  setUserProperty(name: string, value: string): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'setUserProperty', [name, value]);
    });
  }

}

export interface LogEventParams {
  [k: string]: any;
}
