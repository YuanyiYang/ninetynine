function ninetynine(){var bb='',$=' top: -1000px;',yb='" for "gwt:onLoadErrorFn"',wb='" for "gwt:onPropertyErrorFn"',hb='");',zb='#',mc='.cache.js',Bb='/',Hb='//',lc=':',Zb=':1',$b=':10',_b=':11',ac=':12',bc=':13',cc=':2',dc=':3',ec=':4',fc=':5',gc=':6',hc=':7',ic=':8',jc=':9',qb='::',yc=':moduleBase',ab='<!doctype html>',cb='<html><head><\/head><body><\/body><\/html>',tb='=',Ab='?',vb='Bad handler "',_='CSS1Compat',fb='Chrome',Yb='D976955744BDBB895017608152497C90',eb='DOMContentLoaded',V='DUMMY',kc='E9EBA4F95D6A807892F2A4BCC1F22259',xc='Ignoring non-whitelisted Dev Mode URL: ',wc='__gwtDevModeHook:ninetynine',Jb='android',Lb='android_tablet',Gb='base',Eb='baseUrl',Q='begin',Rb='blackberry',W='body',P='bootstrap',Db='clear.cache.gif',sb='content',Sb='desktop',tc='end',gb='eval("',vc='file:',R='gwt.codesvr.ninetynine=',S='gwt.codesvr=',sc='gwt/clean/clean.css',xb='gwt:onLoadErrorFn',ub='gwt:onPropertyErrorFn',rb='gwt:property',mb='head',qc='href',uc='http:',X='iframe',Cb='img',Mb='ipad',Nb='ipad_retina',Ob='iphone',Pb='ipod',jb='javascript',Y='javascript:""',nc='link',rc='loadExternalRefs',nb='meta',Ib='mgwt.os',Kb='mobile',Tb='mobile.user.agent',Ub='mobilesafari',lb='moduleRequested',kb='moduleStartup',ob='name',T='ninetynine',Xb='ninetynine.devmode.js',Fb='ninetynine.nocache.js',pb='ninetynine::',Vb='not_mobile',Z='position:absolute; width:0; height:0; border:none; left: -1000px;',oc='rel',Qb='retina',ib='script',Wb='selectingPermutation',U='startup',pc='stylesheet',db='undefined';var o=window;var p=document;r(P,Q);function q(){var a=o.location.search;return a.indexOf(R)!=-1||a.indexOf(S)!=-1}
function r(a,b){if(o.__gwtStatsEvent){o.__gwtStatsEvent({moduleName:T,sessionId:o.__gwtStatsSessionId,subSystem:U,evtGroup:a,millis:(new Date).getTime(),type:b})}}
ninetynine.__sendStats=r;ninetynine.__moduleName=T;ninetynine.__errFn=null;ninetynine.__moduleBase=V;ninetynine.__softPermutationId=0;ninetynine.__computePropValue=null;ninetynine.__getPropMap=null;ninetynine.__gwtInstallCode=function(){};ninetynine.__gwtStartLoadingFragment=function(){return null};var s=function(){return false};var t=function(){return null};__propertyErrorFunction=null;var u=o.__gwt_activeModules=o.__gwt_activeModules||{};u[T]={moduleName:T};var v;function w(){B();return v}
function A(){B();return v.getElementsByTagName(W)[0]}
function B(){if(v){return}var a=p.createElement(X);a.src=Y;a.id=T;a.style.cssText=Z+$;a.tabIndex=-1;p.body.appendChild(a);v=a.contentDocument;if(!v){v=a.contentWindow.document}v.open();var b=document.compatMode==_?ab:bb;v.write(b+cb);v.close()}
function C(k){function l(a){function b(){if(typeof p.readyState==db){return typeof p.body!=db&&p.body!=null}return /loaded|complete/.test(p.readyState)}
var c=b();if(c){a();return}function d(){if(!c){c=true;a();if(p.removeEventListener){p.removeEventListener(eb,d,false)}if(e){clearInterval(e)}}}
if(p.addEventListener){p.addEventListener(eb,d,false)}var e=setInterval(function(){if(b()){d()}},50)}
function m(c){function d(a,b){a.removeChild(b)}
var e=A();var f=w();var g;if(navigator.userAgent.indexOf(fb)>-1&&window.JSON){var h=f.createDocumentFragment();h.appendChild(f.createTextNode(gb));for(var i=0;i<c.length;i++){var j=window.JSON.stringify(c[i]);h.appendChild(f.createTextNode(j.substring(1,j.length-1)))}h.appendChild(f.createTextNode(hb));g=f.createElement(ib);g.language=jb;g.appendChild(h);e.appendChild(g);d(e,g)}else{for(var i=0;i<c.length;i++){g=f.createElement(ib);g.language=jb;g.text=c[i];e.appendChild(g);d(e,g)}}}
ninetynine.onScriptDownloaded=function(a){l(function(){m(a)})};r(kb,lb);var n=p.createElement(ib);n.src=k;p.getElementsByTagName(mb)[0].appendChild(n)}
ninetynine.__startLoadingFragment=function(a){return G(a)};ninetynine.__installRunAsyncCode=function(a){var b=A();var c=w().createElement(ib);c.language=jb;c.text=a;b.appendChild(c);b.removeChild(c)};function D(){var c={};var d;var e;var f=p.getElementsByTagName(nb);for(var g=0,h=f.length;g<h;++g){var i=f[g],j=i.getAttribute(ob),k;if(j){j=j.replace(pb,bb);if(j.indexOf(qb)>=0){continue}if(j==rb){k=i.getAttribute(sb);if(k){var l,m=k.indexOf(tb);if(m>=0){j=k.substring(0,m);l=k.substring(m+1)}else{j=k;l=bb}c[j]=l}}else if(j==ub){k=i.getAttribute(sb);if(k){try{d=eval(k)}catch(a){alert(vb+k+wb)}}}else if(j==xb){k=i.getAttribute(sb);if(k){try{e=eval(k)}catch(a){alert(vb+k+yb)}}}}}t=function(a){var b=c[a];return b==null?null:b};__propertyErrorFunction=d;ninetynine.__errFn=e}
function F(){function e(a){var b=a.lastIndexOf(zb);if(b==-1){b=a.length}var c=a.indexOf(Ab);if(c==-1){c=a.length}var d=a.lastIndexOf(Bb,Math.min(c,b));return d>=0?a.substring(0,d+1):bb}
function f(a){if(a.match(/^\w+:\/\//)){}else{var b=p.createElement(Cb);b.src=a+Db;a=e(b.src)}return a}
function g(){var a=t(Eb);if(a!=null){return a}return bb}
function h(){var a=p.getElementsByTagName(ib);for(var b=0;b<a.length;++b){if(a[b].src.indexOf(Fb)!=-1){return e(a[b].src)}}return bb}
function i(){var a=p.getElementsByTagName(Gb);if(a.length>0){return a[a.length-1].href}return bb}
function j(){var a=p.location;return a.href==a.protocol+Hb+a.host+a.pathname+a.search+a.hash}
var k=g();if(k==bb){k=h()}if(k==bb){k=i()}if(k==bb&&j()){k=e(p.location.href)}k=f(k);return k}
function G(a){if(a.match(/^\//)){return a}if(a.match(/^[a-zA-Z]+:\/\//)){return a}return ninetynine.__moduleBase+a}
function H(){var f=[];var g;function h(a,b){var c=f;for(var d=0,e=a.length-1;d<e;++d){c=c[a[d]]||(c[a[d]]=[])}c[a[e]]=b}
var i=[];var j=[];function k(a){var b=j[a](),c=i[a];if(b in c){return b}var d=[];for(var e in c){d[c[e]]=e}if(__propertyErrorFunc){__propertyErrorFunc(a,d,b)}throw null}
j[Ib]=function(){{var b=function(){var a=window.navigator.userAgent.toLowerCase();if(a.indexOf(Jb)!=-1){if(a.indexOf(Kb)!=-1){return Jb}else{return Lb}}if(a.indexOf(Mb)!=-1){if(window.devicePixelRatio>=2){return Nb}return Mb}if(a.indexOf(Ob)!=-1||a.indexOf(Pb)!=-1){if(window.devicePixelRatio>=2){return Qb}return Ob}if(a.indexOf(Rb)!=-1){return Rb}return Sb}();return b}};i[Ib]={android:0,android_tablet:1,blackberry:2,desktop:3,ipad:4,ipad_retina:5,iphone:6,retina:7};j[Tb]=function(){return /(android|iphone|ipod|ipad)/i.test(window.navigator.userAgent)?Ub:Vb};i[Tb]={mobilesafari:0,not_mobile:1};s=function(a,b){return b in i[a]};ninetynine.__getPropMap=function(){var a={};for(var b in i){if(i.hasOwnProperty(b)){a[b]=k(b)}}return a};ninetynine.__computePropValue=k;o.__gwt_activeModules[T].bindings=ninetynine.__getPropMap;r(P,Wb);if(q()){return G(Xb)}var l;try{h([Jb,Vb],Yb);h([Lb,Vb],Yb);h([Rb,Ub],Yb);h([Rb,Vb],Yb);h([Sb,Ub],Yb);h([Sb,Vb],Yb);h([Mb,Ub],Yb);h([Mb,Vb],Yb);h([Nb,Ub],Yb);h([Nb,Vb],Yb);h([Ob,Ub],Yb);h([Ob,Vb],Yb);h([Qb,Ub],Yb);h([Qb,Vb],Yb);h([Jb,Vb],Yb+Zb);h([Lb,Vb],Yb+Zb);h([Rb,Ub],Yb+Zb);h([Rb,Vb],Yb+Zb);h([Sb,Ub],Yb+Zb);h([Sb,Vb],Yb+Zb);h([Mb,Ub],Yb+Zb);h([Mb,Vb],Yb+Zb);h([Nb,Ub],Yb+Zb);h([Nb,Vb],Yb+Zb);h([Ob,Ub],Yb+Zb);h([Ob,Vb],Yb+Zb);h([Qb,Ub],Yb+Zb);h([Qb,Vb],Yb+Zb);h([Jb,Vb],Yb+$b);h([Lb,Vb],Yb+$b);h([Rb,Ub],Yb+$b);h([Rb,Vb],Yb+$b);h([Sb,Ub],Yb+$b);h([Sb,Vb],Yb+$b);h([Mb,Ub],Yb+$b);h([Mb,Vb],Yb+$b);h([Nb,Ub],Yb+$b);h([Nb,Vb],Yb+$b);h([Ob,Ub],Yb+$b);h([Ob,Vb],Yb+$b);h([Qb,Ub],Yb+$b);h([Qb,Vb],Yb+$b);h([Jb,Vb],Yb+_b);h([Lb,Vb],Yb+_b);h([Rb,Ub],Yb+_b);h([Rb,Vb],Yb+_b);h([Sb,Ub],Yb+_b);h([Sb,Vb],Yb+_b);h([Mb,Ub],Yb+_b);h([Mb,Vb],Yb+_b);h([Nb,Ub],Yb+_b);h([Nb,Vb],Yb+_b);h([Ob,Ub],Yb+_b);h([Ob,Vb],Yb+_b);h([Qb,Ub],Yb+_b);h([Qb,Vb],Yb+_b);h([Jb,Vb],Yb+ac);h([Lb,Vb],Yb+ac);h([Rb,Ub],Yb+ac);h([Rb,Vb],Yb+ac);h([Sb,Ub],Yb+ac);h([Sb,Vb],Yb+ac);h([Mb,Ub],Yb+ac);h([Mb,Vb],Yb+ac);h([Nb,Ub],Yb+ac);h([Nb,Vb],Yb+ac);h([Ob,Ub],Yb+ac);h([Ob,Vb],Yb+ac);h([Qb,Ub],Yb+ac);h([Qb,Vb],Yb+ac);h([Jb,Vb],Yb+bc);h([Lb,Vb],Yb+bc);h([Rb,Ub],Yb+bc);h([Rb,Vb],Yb+bc);h([Sb,Ub],Yb+bc);h([Sb,Vb],Yb+bc);h([Mb,Ub],Yb+bc);h([Mb,Vb],Yb+bc);h([Nb,Ub],Yb+bc);h([Nb,Vb],Yb+bc);h([Ob,Ub],Yb+bc);h([Ob,Vb],Yb+bc);h([Qb,Ub],Yb+bc);h([Qb,Vb],Yb+bc);h([Jb,Vb],Yb+cc);h([Lb,Vb],Yb+cc);h([Rb,Ub],Yb+cc);h([Rb,Vb],Yb+cc);h([Sb,Ub],Yb+cc);h([Sb,Vb],Yb+cc);h([Mb,Ub],Yb+cc);h([Mb,Vb],Yb+cc);h([Nb,Ub],Yb+cc);h([Nb,Vb],Yb+cc);h([Ob,Ub],Yb+cc);h([Ob,Vb],Yb+cc);h([Qb,Ub],Yb+cc);h([Qb,Vb],Yb+cc);h([Jb,Vb],Yb+dc);h([Lb,Vb],Yb+dc);h([Rb,Ub],Yb+dc);h([Rb,Vb],Yb+dc);h([Sb,Ub],Yb+dc);h([Sb,Vb],Yb+dc);h([Mb,Ub],Yb+dc);h([Mb,Vb],Yb+dc);h([Nb,Ub],Yb+dc);h([Nb,Vb],Yb+dc);h([Ob,Ub],Yb+dc);h([Ob,Vb],Yb+dc);h([Qb,Ub],Yb+dc);h([Qb,Vb],Yb+dc);h([Jb,Vb],Yb+ec);h([Lb,Vb],Yb+ec);h([Rb,Ub],Yb+ec);h([Rb,Vb],Yb+ec);h([Sb,Ub],Yb+ec);h([Sb,Vb],Yb+ec);h([Mb,Ub],Yb+ec);h([Mb,Vb],Yb+ec);h([Nb,Ub],Yb+ec);h([Nb,Vb],Yb+ec);h([Ob,Ub],Yb+ec);h([Ob,Vb],Yb+ec);h([Qb,Ub],Yb+ec);h([Qb,Vb],Yb+ec);h([Jb,Vb],Yb+fc);h([Lb,Vb],Yb+fc);h([Rb,Ub],Yb+fc);h([Rb,Vb],Yb+fc);h([Sb,Ub],Yb+fc);h([Sb,Vb],Yb+fc);h([Mb,Ub],Yb+fc);h([Mb,Vb],Yb+fc);h([Nb,Ub],Yb+fc);h([Nb,Vb],Yb+fc);h([Ob,Ub],Yb+fc);h([Ob,Vb],Yb+fc);h([Qb,Ub],Yb+fc);h([Qb,Vb],Yb+fc);h([Jb,Vb],Yb+gc);h([Lb,Vb],Yb+gc);h([Rb,Ub],Yb+gc);h([Rb,Vb],Yb+gc);h([Sb,Ub],Yb+gc);h([Sb,Vb],Yb+gc);h([Mb,Ub],Yb+gc);h([Mb,Vb],Yb+gc);h([Nb,Ub],Yb+gc);h([Nb,Vb],Yb+gc);h([Ob,Ub],Yb+gc);h([Ob,Vb],Yb+gc);h([Qb,Ub],Yb+gc);h([Qb,Vb],Yb+gc);h([Jb,Vb],Yb+hc);h([Lb,Vb],Yb+hc);h([Rb,Ub],Yb+hc);h([Rb,Vb],Yb+hc);h([Sb,Ub],Yb+hc);h([Sb,Vb],Yb+hc);h([Mb,Ub],Yb+hc);h([Mb,Vb],Yb+hc);h([Nb,Ub],Yb+hc);h([Nb,Vb],Yb+hc);h([Ob,Ub],Yb+hc);h([Ob,Vb],Yb+hc);h([Qb,Ub],Yb+hc);h([Qb,Vb],Yb+hc);h([Jb,Vb],Yb+ic);h([Lb,Vb],Yb+ic);h([Rb,Ub],Yb+ic);h([Rb,Vb],Yb+ic);h([Sb,Ub],Yb+ic);h([Sb,Vb],Yb+ic);h([Mb,Ub],Yb+ic);h([Mb,Vb],Yb+ic);h([Nb,Ub],Yb+ic);h([Nb,Vb],Yb+ic);h([Ob,Ub],Yb+ic);h([Ob,Vb],Yb+ic);h([Qb,Ub],Yb+ic);h([Qb,Vb],Yb+ic);h([Jb,Vb],Yb+jc);h([Lb,Vb],Yb+jc);h([Rb,Ub],Yb+jc);h([Rb,Vb],Yb+jc);h([Sb,Ub],Yb+jc);h([Sb,Vb],Yb+jc);h([Mb,Ub],Yb+jc);h([Mb,Vb],Yb+jc);h([Nb,Ub],Yb+jc);h([Nb,Vb],Yb+jc);h([Ob,Ub],Yb+jc);h([Ob,Vb],Yb+jc);h([Qb,Ub],Yb+jc);h([Qb,Vb],Yb+jc);h([Jb,Ub],kc);h([Lb,Ub],kc);h([Jb,Ub],kc+Zb);h([Lb,Ub],kc+Zb);l=f[k(Ib)][k(Tb)];var m=l.indexOf(lc);if(m!=-1){g=parseInt(l.substring(m+1),10);l=l.substring(0,m)}}catch(a){}ninetynine.__softPermutationId=g;return G(l+mc)}
function I(){if(!o.__gwt_stylesLoaded){o.__gwt_stylesLoaded={}}function c(a){if(!__gwt_stylesLoaded[a]){var b=p.createElement(nc);b.setAttribute(oc,pc);b.setAttribute(qc,G(a));p.getElementsByTagName(mb)[0].appendChild(b);__gwt_stylesLoaded[a]=true}}
r(rc,Q);c(sc);r(rc,tc)}
D();ninetynine.__moduleBase=F();u[T].moduleBase=ninetynine.__moduleBase;var J=H();if(o){var K=!!(o.location.protocol==uc||o.location.protocol==vc);o.__gwt_activeModules[T].canRedirect=K;if(K){var L=wc;var M=o.sessionStorage[L];if(!/^http:\/\/(localhost|127\.0\.0\.1)(:\d+)?\/.*$/.test(M)){if(M&&(window.console&&console.log)){console.log(xc+M)}M=bb}if(M&&!o[L]){o[L]=true;o[L+yc]=F();var N=p.createElement(ib);N.src=M;var O=p.getElementsByTagName(mb)[0];O.insertBefore(N,O.firstElementChild||O.children[0]);return false}}}I();r(P,tc);C(J);return true}
ninetynine.succeeded=ninetynine();