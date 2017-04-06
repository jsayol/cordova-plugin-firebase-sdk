import { exec, SuccessCallback, ErrorCallback } from '../utils';

export class Messaging {
  private static _instance: Messaging;

  constructor() {
    // Prevent creating multiple instances
    if (Messaging._instance) {
      return Messaging._instance;
    }
  }

  static getInstance(): Messaging {
    if (!this._instance) {
      this._instance = new Messaging();
    }

    return this._instance;
  }

  getToken(): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'messaging_getToken', []);
    });
  }

  onNotificationOpen(callback: SuccessCallback, error: ErrorCallback): void {
    exec(callback, error, 'Firebase', 'messaging_onNotificationOpen', []);
  }

  onTokenRefresh(callback: SuccessCallback, error: ErrorCallback): void {
    exec(callback, error, 'Firebase', 'messaging_onTokenRefresh', []);
  }

  grantPermission(): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'messaging_grantPermission', []);
    });
  }

  hasPermission(): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'messaging_hasPermission', []);
    });
  }

  setBadgeNumber(number: number): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'messaging_setBadgeNumber', [number]);
    });
  }

  getBadgeNumber(): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'messaging_getBadgeNumber', []);
    });
  }

  subscribe(topic: string): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'messaging_subscribe', [topic]);
    });
  }

  unsubscribe(topic: string): Promise<any> {
    return new Promise<any>((resolve: SuccessCallback, reject: ErrorCallback) => {
      exec(resolve, reject, 'Firebase', 'messaging_unsubscribe', [topic]);
    });
  }

}

export const messaging = () => Messaging.getInstance();
