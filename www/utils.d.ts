import exec from 'cordova/exec';
export { exec };
export declare type SuccessCallback = (value?: any) => any;
export declare type ErrorCallback = (reason?: any) => any;
export declare function getValueType(value: any): string;
export declare const isInteger: (number: number) => boolean;
