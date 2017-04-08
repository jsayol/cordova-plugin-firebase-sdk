import { SuccessCallback, ErrorCallback } from '../utils';
import { App } from '../app';
export declare class Messaging {
    private _app;
    private static _instance;
    private constructor(_app);
    static getInstance(app: App): Messaging;
    private _exec(success, error, action, args?);
    getToken(): Promise<any>;
    onNotificationOpen(callback: SuccessCallback, error: ErrorCallback): void;
    onTokenRefresh(callback: SuccessCallback, error: ErrorCallback): void;
    grantPermission(): Promise<any>;
    hasPermission(): Promise<any>;
    setBadgeNumber(number: number): Promise<any>;
    getBadgeNumber(): Promise<any>;
    subscribe(topic: string): Promise<any>;
    unsubscribe(topic: string): Promise<any>;
}
