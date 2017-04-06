import { SuccessCallback, ErrorCallback } from '../utils';
export declare class Messaging {
    private static _instance;
    constructor();
    static getInstance(): Messaging;
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
export declare const messaging: () => Messaging;
