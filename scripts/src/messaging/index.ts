import { SuccessCallback, ErrorCallback } from '../utils';
import { App } from '../app';

export class Messaging {
  private static _instance: Messaging;

  private constructor(private _app: App) {
  }

  static getInstance(app: App): Messaging {
    if (!this._instance) {
      this._instance = new Messaging(app);
    }
    return this._instance;
  }

  private _exec(success: SuccessCallback, error: ErrorCallback, action: string, args?: any[]) {
    this._app._execWithoutName(success, error, `messaging_${action}`, args);
  }

  getToken(): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'getToken');
    });
  }

  onNotificationOpen(callback: SuccessCallback, error: ErrorCallback): void {
    this._exec(callback, error, 'onNotificationOpen');
  }

  onTokenRefresh(callback: SuccessCallback, error: ErrorCallback): void {
    this._exec(callback, error, 'onTokenRefresh');
  }

  grantPermission(): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'grantPermission');
    });
  }

  hasPermission(): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'hasPermission');
    });
  }

  setBadgeNumber(number: number): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'setBadgeNumber', [number]);
    });
  }

  getBadgeNumber(): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'getBadgeNumber');
    });
  }

  subscribe(topic: string): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'subscribe', [topic]);
    });
  }

  unsubscribe(topic: string): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      this._exec(resolve, reject, 'unsubscribe', [topic]);
    });
  }

}
