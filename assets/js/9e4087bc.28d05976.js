"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[2711],{9331:(e,r,a)=>{a.r(r),a.d(r,{default:()=>m});a(6540);var t=a(8774),n=a(1312),i=a(1213),s=a(6266),c=a(781),l=a(1107),o=a(4848);function d(e){var r=e.year,a=e.posts,n=(0,s.i)({day:"numeric",month:"long",timeZone:"UTC"});return(0,o.jsxs)(o.Fragment,{children:[(0,o.jsx)(l.A,{as:"h3",id:r,children:r}),(0,o.jsx)("ul",{children:a.map((function(e){return(0,o.jsx)("li",{children:(0,o.jsxs)(t.A,{to:e.metadata.permalink,children:[(r=e.metadata.date,n.format(new Date(r)))," - ",e.metadata.title]})},e.metadata.date);var r}))})]})}function h(e){var r=e.years;return(0,o.jsx)("section",{className:"margin-vert--lg",children:(0,o.jsx)("div",{className:"container",children:(0,o.jsx)("div",{className:"row",children:r.map((function(e,r){return(0,o.jsx)("div",{className:"col col--4 margin-vert--lg",children:(0,o.jsx)(d,Object.assign({},e))},r)}))})})})}function m(e){var r,a,t=e.archive,s=(0,n.T)({id:"theme.blog.archive.title",message:"Archive",description:"The page & hero title of the blog archive page"}),d=(0,n.T)({id:"theme.blog.archive.description",message:"Archive",description:"The page & hero description of the blog archive page"}),m=(r=t.blogPosts,a=r.reduce((function(e,r){var a,t=r.metadata.date.split("-")[0],n=null!=(a=e.get(t))?a:[];return e.set(t,[r].concat(n))}),new Map),Array.from(a,(function(e){return{year:e[0],posts:e[1]}})));return(0,o.jsxs)(o.Fragment,{children:[(0,o.jsx)(i.be,{title:s,description:d}),(0,o.jsxs)(c.A,{children:[(0,o.jsx)("header",{className:"hero hero--primary",children:(0,o.jsxs)("div",{className:"container",children:[(0,o.jsx)(l.A,{as:"h1",className:"hero__title",children:s}),(0,o.jsx)("p",{className:"hero__subtitle",children:d})]})}),(0,o.jsx)("main",{children:m.length>0&&(0,o.jsx)(h,{years:m})})]})]})}},6266:(e,r,a)=>{a.d(r,{i:()=>n});var t=a(4586);function n(e){void 0===e&&(e={});var r=(0,t.A)().i18n.currentLocale,a=function(){var e=(0,t.A)().i18n,r=e.currentLocale;return e.localeConfigs[r].calendar}();return new Intl.DateTimeFormat(r,Object.assign({calendar:a},e))}}}]);