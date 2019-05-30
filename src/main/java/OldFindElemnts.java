//import javafx.util.Pair;
//
//public class OldFindElemnts {
//    public static void findElements(Pair[] clickPare) {
//       /* boolean open = true;
//int n=0;
//        String value = driver.getPageSource();
//        value = value.substring(value.indexOf("id=\"game\""), value.indexOf("display: none;"));
//
//        while (open == true) {
//            if (open == true) n++;
//            for (Pair<Integer, Integer> stc : clickPare) {
//                if ((x > 0 && y > 0 && x < stc.getKey() && y < stc.getValue()) == false) {
//                    int x0 = (stc.getKey() + 1) - n;
//                    int y0 = (stc.getValue() + 1) - n;
//
//                    int x1 = (stc.getKey() + 1) + n;
//                    int y1 = (stc.getValue() + 1) + n;
//
//                    for (int i = y0; i <= y1; i++) {
//                        for (int j = x0; j <= x1; j++) {
//                            if (field[j - 1][i - 1] == -1) {
//
//
//                                int index = value.indexOf("\" id=\"" + i + "_" + j + "\"") - 1;
//                                switch (value.charAt(index)) {
//                                    case 'k': {open =false;
//                                        break;
//                                    }
//                                    case '0': { open =true;
//                                        field[j - 1][i - 1] = 0;
//                                        break;
//                                    }
//                                    case '1': {open =true;
//                                        field[j - 1][i - 1] = 1;
//                                        break;
//                                    }
//                                    case '2': {open =true;
//                                        field[j - 1][i - 1] = 2;
//                                        break;
//                                    }
//                                    case '3': {open =true;
//                                        field[j - 1][i - 1] = 3;
//                                        break;
//                                    }
//                                    case '4': {open =true;
//                                        field[j - 1][i - 1] = 4;
//                                        break;
//                                    }
//                                    case '5': {open =true;
//                                        field[j - 1][i - 1] = 5;
//                                        break;
//                                    }
//                                    case '6': {open =true;
//                                        field[j - 1][i - 1] = 6;
//                                        break;
//                                    }
//                                    case '7': {open =true;
//                                        field[j - 1][i - 1] = 7;
//                                        break;
//                                    }
//                                    case '8': {open =true;
//                                        field[j - 1][i - 1] = 8;
//                                        break;
//                                    }
//                                }
//
//
//                            }System.out.print(field[j - 1][i - 1] + " ");
//                        }System.out.println();
//                    }
//
//
//                }
//
//                //  driver.findElement(By.id(stc.getValue() + 1 + "_" + (stc.getKey() + 1))).click();
//            }
//        }*/
//
//
//        String value = driver.getPageSource();
//        value = value.substring(value.indexOf("id=\"game\""), value.indexOf("display: none;"));
//
//        for (int i = 1; i <= y; i++) {
//            for (int j = 1; j <= x; j++) {
//                if (field[j - 1][i - 1] == -1) {
//
//
//                    int index = value.indexOf("\" id=\"" + i + "_" + j + "\"") - 1;
//                    switch (value.charAt(index)) {
//                        case 'k': {
//                            break;
//                        }
//                        case '0': {
//                            field[j - 1][i - 1] = 0;
//                            break;
//                        }
//                        case '1': {
//                            field[j - 1][i - 1] = 1;
//                            break;
//                        }
//                        case '2': {
//                            field[j - 1][i - 1] = 2;
//                            break;
//                        }
//                        case '3': {
//                            field[j - 1][i - 1] = 3;
//                            break;
//                        }
//                        case '4': {
//                            field[j - 1][i - 1] = 4;
//                            break;
//                        }
//                        case '5': {
//                            field[j - 1][i - 1] = 5;
//                            break;
//                        }
//                        case '6': {
//                            field[j - 1][i - 1] = 6;
//                            break;
//                        }
//                        case '7': {
//                            field[j - 1][i - 1] = 7;
//                            break;
//                        }
//                        case '8': {
//                            field[j - 1][i - 1] = 8;
//                            break;
//                        }
//                    }
////                    if (value.contains("class=\"square blank\" id=\"" + i + "_" + j + "\"")) field[j - 1][i - 1] = -1;else {
////                    if (value.contains("class=\"square open0\" id=\"" + i + "_" + j + "\"")) field[j - 1][i - 1] = 0;else {
////                    if (value.contains("class=\"square open1\" id=\"" + i + "_" + j + "\"")) field[j - 1][i - 1] = 1;else {
////                    if (value.contains("class=\"square open2\" id=\"" + i + "_" + j + "\"")) field[j - 1][i - 1] = 2;else {
////                    if (value.contains("class=\"square open3\" id=\"" + i + "_" + j + "\"")) field[j - 1][i - 1] = 3;else {
////                    if (value.contains("class=\"square open4\" id=\"" + i + "_" + j + "\"")) field[j - 1][i - 1] = 4;else {
////                    if (value.contains("class=\"square open5\" id=\"" + i + "_" + j + "\"")) field[j - 1][i - 1] = 5;else {
////                    if (value.contains("class=\"square open6\" id=\"" + i + "_" + j + "\"")) field[j - 1][i - 1] = 6;else {
////                    if (value.contains("class=\"square open7\" id=\"" + i + "_" + j + "\"")) field[j - 1][i - 1] = 7;else {
////                        if (value.contains("class=\"square open8\" id=\"" + i + "_" + j + "\""))
////                            field[j - 1][i - 1] = 8;
////                    }}}}}}}}}
//                }
//                //System.out.print(field[j - 1][i - 1] + " ");
//            }
//            // System.out.println();
//        }
//
//    }
//}
