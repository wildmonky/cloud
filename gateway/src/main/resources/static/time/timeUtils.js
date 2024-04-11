/**
 * 将时间转换为本地时间
 * @param timeWithZone pattern: yyyy-MM-ddTHH:mm:ss[zone] zone like +08:00
 */
function transformToLocalTime(timeWithZone) {
    return new Date(timeWithZone);
}